package net.fortytwo.ripple.model.impl.sesame;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Buffer;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.flow.rdf.CloseableIterationSource;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.control.Task;
import net.fortytwo.ripple.control.TaskSet;
import net.fortytwo.ripple.io.RDFImporter;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.GetStatementsQuery;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleComparator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.model.types.NumericType;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.impl.MapBindingSet;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.sparql.SPARQLParser;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailConnectionListener;
import org.openrdf.sail.SailException;
import org.openrdf.sail.SailReadOnlyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameModelConnection implements ModelConnection {
    private static final Logger logger = LoggerFactory.getLogger(ModelConnection.class);

    // instantiate this factory lazily, for the sake of Android applications which don't support javax
    private static DatatypeFactory DATATYPE_FACTORY;

    private final SesameModel model;
    private SailConnection sailConnection;
    private final RDFDiffSink listenerSink;
    private final ValueFactory valueFactory;
    private final TaskSet taskSet = new TaskSet();
    private final RippleComparator comparator;

    private boolean closed = false;

    protected SesameModelConnection(final SesameModel model, final RDFDiffSink listenerSink)
            throws RippleException {
        this.model = model;
        this.listenerSink = listenerSink;

        try {
            valueFactory = model.sail.getValueFactory();
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        openSailConnection();

        synchronized (model.openConnections) {
            model.openConnections.add(this);
        }

        comparator = new RippleComparator(this);
    }

    public ValueFactory getValueFactory() {
        return valueFactory;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void toList(final Object v,
                       final Sink<RippleList> sink) throws RippleException {
        SesameList.from(v, sink, this);
    }

    @Override
    public RippleList list() {
        return SesameList.nilList();
    }

    @Override
    public void finish() throws RippleException {
        taskSet.waitUntilEmpty();
    }

    @Override
    public void close() throws RippleException {
        closed = true;

        finish();

        closeSailConnection(true);

        synchronized (model.openConnections) {
            model.openConnections.remove(this);
        }
    }

    /**
     * Returns the ModelConnection to a normal state after an Exception has
     * been thrown.
     */
    @Override
    public void reset(final boolean rollback) throws RippleException {
        closeSailConnection(rollback);
        openSailConnection();
    }

    @Override
    public void commit() throws RippleException {
        try {
            sailConnection.commit();
            sailConnection.begin();
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException();
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    @Override
    public void print(Object v, RipplePrintStream ps) throws RippleException {
        if (null == v || null == ps) {
            throw new IllegalArgumentException();
        }

        RippleType type = model.getTypeOf(v);
        if (null == type) {
            ps.print(v);
        } else {
            type.print(v, ps, this);
        }
    }

    @Override
    public Value toRDF(Object v) throws RippleException {
        if (null == v) {
            throw new IllegalArgumentException();
        }

        RippleType type = model.getTypeOf(v);
        if (null == type) {
            logger.warn("no type for object of class " + v.getClass());
            return null;
        } else {
            return type.toRDF(v, this);
        }
    }

    @Override
    public StackMapping toMapping(Object v) {
        if (null == v) {
            throw new IllegalArgumentException();
        }

        RippleType type = model.getTypeOf(v);
        if (null == type) {
            logger.warn("no type for object of class " + v.getClass());

            // TODO: temporary
            return null;
        } else {
            return type.getMapping(v);
        }
    }

    /**
     * @return the existing <code>SailConnection</code> of this model connection.
     * A new <code>SailConnection</code> is not created.
     * The connection comes with an active transaction,
     * and a new transaction should be begun immediately after external commit or rollback operations.
     */
    public SailConnection getSailConnection() {
        return sailConnection;
    }

    // Note: everything apart from xsd:true is considered false.
    // Eventually, this method may throw a type mismatch exception if it is
    // given a value other than "true"^^xsd:boolean or "false"^^xsd:boolean.

    @Override
    public boolean toBoolean(final Object rv) throws RippleException {
        if (rv instanceof Boolean) {
            return (Boolean) rv;
        } else {
            Value rdf = toRDF(rv);
            if (null != rdf) {
                if (rdf instanceof Literal) {
                    IRI datatype = ((Literal) rdf).getDatatype();
                    return (null != datatype && XMLSchema.BOOLEAN.equals(datatype)
                            && ((Literal) rdf).getLabel().equals("true"));
                }
            }

            return false;
        }
    }

    @Override
    public Number toNumber(final Object instance) throws RippleException {
        RippleType type = model.getTypeOf(instance);
        if (null == type) {
            throw new RippleException("not of a supported number type: " + instance
                    + "(class " + instance.getClass() + ")");
        } else if (type instanceof NumericType) {
            NumericType.Datatype p = ((NumericType) type).findDatatype(instance);
            return ((NumericType) type).findNumber(instance, p);
        } else {
            throw new RippleException("type is not numeric: " + type);
        }
    }

    @Override
    public Date toDate(final Object v) throws RippleException {
        Literal l = castToLiteral(toRDF(v));

        XMLGregorianCalendar c = l.calendarValue();
        return c.toGregorianCalendar().getTime();
    }

    @Override
    public String toString(final Object v) {
        return v instanceof String
                ? (String) v
                : v instanceof Value
                ? ((Value) v).stringValue()
                : v.toString();
    }

    @Override
    public void add(final Object subj, final Object pred, final Object obj, Object... contexts)
            throws RippleException {
        ensureOpen();

        Value subjValue = toRDF(subj);
        Value predValue = toRDF(pred);
        Value objValue = toRDF(obj);

        if (!(subjValue instanceof Resource)
                || !(predValue instanceof IRI)) {
            return;
        }

        try {
            // Trick to be able to iterate through contexts even if none are given
            if (0 == contexts.length) {
                sailConnection.addStatement(
                        (Resource) subjValue, (IRI) predValue, objValue);
            } else {
                for (Object context : contexts) {
                    Value contextValue;

                    if (null == context) {
                        contextValue = null;
                    } else {
                        contextValue = toRDF(context);

                        // rdf:nil is a special case -- as a analysis name in Ripple, it
                        // actually represents the null analysis.
                        if (contextValue.equals(RDF.NIL)) {
                            contextValue = null;
                        } else if (!(contextValue instanceof Resource)) {
                            return;
                        }
                    }

                    sailConnection.addStatement(
                            (Resource) subjValue, (IRI) predValue, objValue, (Resource) contextValue);
                }
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException();
        } catch (SailException e) {
            reset(true);
            throw new RippleException(e);
        }
    }

    @Override
    public void remove(final Object subj,
                       final Object pred,
                       final Object obj,
                       final Object... contexts) throws RippleException {

        ensureOpen();

        Value subjValue, predValue, objValue;

        if (null == subj) {
            subjValue = null;
        } else {
            subjValue = toRDF(subj);
            if (!(subjValue instanceof Resource)) {
                return;
            }
        }

        if (null == pred) {
            predValue = null;
        } else {
            predValue = toRDF(pred);
            if (!(predValue instanceof IRI)) {
                return;
            }
        }

        if (null == obj) {
            objValue = null;
        } else {
            objValue = toRDF(obj);
        }

        try {
            // Trick to be able to iterate through contexts even if none are given
            if (0 == contexts.length) {
                sailConnection.removeStatements(
                        (Resource) subjValue, (IRI) predValue, objValue);
            } else {
                for (Object context : contexts) {
                    Value contextValue;

                    if (null == context) {
                        contextValue = null;
                    } else {
                        contextValue = toRDF(context);

                        // rdf:nil is a special case -- as a analysis name in Ripple, it
                        // actually represents the null analysis.
                        if (contextValue.equals(RDF.NIL)) {
                            contextValue = null;
                        } else if (!(contextValue instanceof Resource)) {
                            return;
                        }
                    }

                    sailConnection.removeStatements(
                            (Resource) subjValue, (IRI) predValue, objValue, (Resource) contextValue);
                }
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException();
        } catch (SailException e) {
            reset(true);
            throw new RippleException(e);
        }
    }

    // Note: this method is no longer in the ModelConnection API
    public long countStatements(final Resource... contexts)
            throws RippleException {
        ensureOpen();

        int count = 0;

        try {
            //synchronized ( model )
            {
                CloseableIteration<? extends Statement, SailException> stmtIter
                        = sailConnection.getStatements(
                        null, null, null, false, contexts);

                while (stmtIter.hasNext()) {
                    stmtIter.next();
                    count++;
                }

                stmtIter.close();
            }
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }

        return count;
    }

    @Override
    public RippleComparator getComparator() {
        return comparator;
    }

    @Override
    public IRI valueOf(final java.net.URI s) throws RippleException {
        try {
            // Note: do NOT automatically canonicalize values.  Sometimes one needs the original IRI (e.g. so as to
            // remove statements), and not the native object it maps to.
            return valueFactory.createIRI(s.toString());
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    @Override
    public Literal valueOf(final Date d) throws RippleException {
        // synchronize on the only other static member
        synchronized (logger) {
            if (null == DATATYPE_FACTORY) {
                try {
                    DATATYPE_FACTORY = DatatypeFactory.newInstance();
                } catch (DatatypeConfigurationException e) {
                    throw new RippleException(e);
                }
            }
        }

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        XMLGregorianCalendar xml = DATATYPE_FACTORY.newXMLGregorianCalendar(c);
        return valueFactory.createLiteral(xml);
    }

    @Override
    public Object canonicalValue(final Value v) {
        return model.specialValues.get(v);
    }

    @Override
    public Literal valueOf(final String s, final String language) throws RippleException {
        try {
            return valueFactory.createLiteral(s, language);
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    @Override
    public Literal valueOf(final String s, final IRI dataType)
            throws RippleException {
        try {
            return valueFactory.createLiteral(s, dataType);
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    @Override
    public void setNamespace(final String prefix, final String ns, final boolean override)
            throws RippleException {
        ensureOpen();

        //logger.info( "### setting namespace: '" + prefix + "' to " + ns );
        try {
            //synchronized ( model )
            {
                if (override || null == sailConnection.getNamespace(prefix)) {
                    if (null == ns) {
                        sailConnection.removeNamespace(prefix);
                    } else {
                        sailConnection.setNamespace(prefix, ns);
                    }
                }
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException();
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    @Override
    public void query(final StatementPatternQuery query,
                      final Sink sink,
                      final boolean asynchronous) throws RippleException {
        ensureOpen();

        if (asynchronous) {
            QueryTask task = new QueryTask(query, sink);
            taskSet.add(task);
        } else {
            GetStatementsQuery sesameQuery;

            try {
                sesameQuery = new GetStatementsQuery(query, this);
            } catch (GetStatementsQuery.InvalidQueryException e) {
                logger.warn("invalid query: " + e.getMessage(), e);
                return;
            }

            Sink<Value> valueSink = val -> sink.accept(canonicalValue(val));

            try {
                sesameQuery.getValues(sailConnection, valueSink);
            } catch (RippleException e) {
                reset(true);
                throw e;
            }
        }
    }

    @Override
    public Source<Namespace> getNamespaces() throws RippleException {
        ensureOpen();

        Collector<Namespace> results = new Collector<>();
        Source<Namespace> source;

        try {
            source = new CloseableIterationSource<>(
                    (CloseableIteration<Namespace, SailException>) sailConnection.getNamespaces());
        } catch (SailException e) {
            throw new RippleException(e);
        }

        source.writeTo(results);

        return results;
    }

    //FIXME: Statements should be absent from the ModelConnection API
    @Override
    public void getStatements(final Value subj,
                              final Value pred,
                              final Value obj,
                              final Sink<Statement> sink)
            throws RippleException {
        ensureOpen();

        Value rdfSubj = (null == subj) ? null : subj;
        Value rdfPred = (null == pred) ? null : pred;
        Value rdfObj = (null == obj) ? null : obj;

        if ((null == rdfSubj || rdfSubj instanceof Resource)
                && (null == rdfPred || rdfPred instanceof IRI)) {
            // Note: we must collect results in a buffer before putting anything
            //       into the sink, as inefficient as that is, because otherwise
            //       we might end up opening another RepositoryResult before
            //       the one below closes, which currently causes Sesame to
            //       deadlock.  Even using a separate RepositoryConnection for
            //       each RepositoryResult doesn't seem to help.
            Buffer<Statement> buffer = new Buffer<>(sink);
            CloseableIteration<? extends Statement, SailException> stmtIter;

            //TODO: use CloseableIterationSource

            try {
                // Perform the query and collect results.
                stmtIter = sailConnection.getStatements(
                        (Resource) rdfSubj, (IRI) rdfPred, rdfObj, false);
                //stmtIter.enableDuplicateFilter();
                try {
                    while (stmtIter.hasNext()) {
                        Statement st = stmtIter.next();
                        try {
                            buffer.accept(st);
                        } catch (RippleException e) {
                            // Soft fail
                            logger.warn("buffer failure", e);
                        }
                    }
                } finally {
                    stmtIter.close();
                }
            } catch (SailException e) {
                throw new RippleException(e);
            }

            buffer.flush();
        }
    }

    @Override
    public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(final String query)
            throws RippleException {
        ensureOpen();

        SPARQLParser parser = new SPARQLParser();

        boolean useInference = false;
        // FIXME
        String baseIRI = "http://example.org/bogusBaseIRI/";

        ParsedQuery pq;
        try {
            pq = parser.parseQuery(query, baseIRI);
        } catch (MalformedQueryException e) {
            throw new RippleException(e);
        }

        MapBindingSet bindings = new MapBindingSet();

        try {
            return sailConnection.evaluate(pq.getTupleExpr(), pq.getDataset(), bindings, useInference);
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }

    @Override
    public Source<Object> getContexts() {
        ensureOpen();

        return sink -> {
            try {
                CloseableIteration<? extends Resource, SailException> iter
                        = sailConnection.getContextIDs();

                while (iter.hasNext()) {
                    sink.accept(iter.next());
                }

                iter.close();
            } catch (SailException e) {
                throw new RippleException(e);
            }
        };
    }

    @Override
    public void internalize(final RippleList list) throws RippleException {
        ensureOpen();

        Collector<Statement> buffer = new Collector<>();

        // Handle circular lists (in the unlikely event that some implementation allows them) sanely.
        // TODO: handle list containment cycles (e.g. list containing a list containing the original list) as well.
        // These are actually more likely than circular lists.
        Set<Value> alreadyInterned = new HashSet<>();

        RippleList cur = list;
        Value id = toRDF(cur);
        while (!cur.isNil()) {
            if (alreadyInterned.contains(id)) {
                break;
            } else {
                alreadyInterned.add(id);
            }

            Value firstRdf = toRDF(cur.getFirst());

            if (null == firstRdf) {
                System.err.println("list item has no RDF identity: " + cur.getFirst());
                return;
            }

            if (cur.getFirst() instanceof RippleList) {
                internalize((RippleList) cur.getFirst());
            }

            RippleList rest = cur.getRest();
            Value restRdf = toRDF(rest);

            buffer.accept(
                    valueFactory.createStatement((Resource) id, RDF.TYPE, RDF.LIST));
            buffer.accept(
                    valueFactory.createStatement((Resource) id, RDF.FIRST, firstRdf));
            buffer.accept(
                    valueFactory.createStatement((Resource) id, RDF.REST, restRdf));

            cur = rest;
            id = restRdf;
        }

        RDFImporter importer = new RDFImporter(this);
        buffer.writeTo(importer.statementSink());

    }

    private synchronized void openSailConnection()
            throws RippleException {
        try {
            sailConnection = model.sail.getConnection();
            sailConnection.begin();

// FIXME: this doesn't give the LexiconUpdater any information about namespaces
// FIXME: removed because SailConnectionListener is no longer supported by arbitrary Sails... you would need to wrap
//        the Sail in a notifying Sail... but this still wouldn't give you namespace updates.
            if (null != listenerSink && sailConnection instanceof NotifyingSailConnection) {
                SailConnectionListener listener
                        = new SailConnectionListenerAdapter(listenerSink);

                ((NotifyingSailConnection) sailConnection).addConnectionListener(listener);
            }
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    private synchronized void closeSailConnection(final boolean rollback)
            throws RippleException {
        try {
            if (sailConnection.isOpen()) {
                if (rollback) {
                    sailConnection.rollback();
                }

                sailConnection.close();
            } else {
                // Don't throw an exception: we could easily end up in a loop.
                logger.error("tried to close an already-closed connection");
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException();
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    private Literal castToLiteral(final Value v) throws RippleException {
        if (v instanceof Literal) {
            return (Literal) v;
        } else {
            throw new RippleException("value " + v + " is not a Literal");
        }
    }

    private void handleSailReadOnlyException() {
        // For now, ignore these exceptions.
    }

    // if the connection is closed, and a thread tries to access it, bail out ASAP before something confusing happens
    // use this method with methods which read from or write to the Sail
    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("connection closed");
        }
    }

    private class QueryTask<T> extends Task {
        private final StatementPatternQuery query;
        private Sink<T> sink;

        public QueryTask(final StatementPatternQuery query, final Sink<T> sink) {
            this.query = query;
            this.sink = sink;
        }

        public void executeProtected() throws RippleException {
            query(query, sink, false);

            /*
            ModelConnection mc = model.getConnection();
            try {
                mc.query(query, sink, false);
            } finally {
                mc.close();
            }*/
        }

        protected void stopProtected() {
            synchronized (query) {
                sink = new NullSink<>();
            }
        }
    }
}
