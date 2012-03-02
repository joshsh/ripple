/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


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
import net.fortytwo.ripple.model.GetStatementsQuery;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleValueComparator;
import net.fortytwo.ripple.model.StatementPatternQuery;
import org.apache.log4j.Logger;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
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

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SesameModelConnection implements ModelConnection {
    private static final Logger LOGGER
            = Logger.getLogger(ModelConnection.class);

    protected final SesameModel model;
    protected SailConnection sailConnection;
    protected final RDFDiffSink listenerSink;
    protected final ValueFactory valueFactory;
    private final TaskSet taskSet = new TaskSet();
    private final Comparator<RippleValue> comparator;

    ////////////////////////////////////////////////////////////////////////////

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

        comparator = new RippleValueComparator(this);
    }

    public Model getModel() {
        return model;
    }

    ValueFactory getValueFactory() {
        return valueFactory;
    }

    public void toList(final RippleValue v,
                       final Sink<RippleList> sink) throws RippleException {
        SesameList.from(v, sink, this);
    }

    public RippleList list() {
        return SesameList.nilList();
    }

    public void finish() throws RippleException {
        taskSet.waitUntilEmpty();
    }

    public void close() throws RippleException {
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
    public void reset(final boolean rollback) throws RippleException {
        closeSailConnection(rollback);
        openSailConnection();
    }

    public void commit() throws RippleException {
        try {
            sailConnection.commit();
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException(e);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    public SailConnection getSailConnection() {
        return sailConnection;
    }

    private synchronized void openSailConnection()
            throws RippleException {
        try {
            sailConnection = model.sail.getConnection();

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
                LOGGER.error("tried to close an already-closed connection");
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException(e);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private Literal castToLiteral(final Value v) throws RippleException {
        if (v instanceof Literal) {
            return (Literal) v;
        } else {
            throw new RippleException("value " + v + " is not a Literal");
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    // Note: everything apart from xsd:true is considered false.
    // Eventually, this method may throw a type mismatch exception if it is
    // given a value other than "true"^^xsd:boolean or "false"^^xsd:boolean.

    public boolean toBoolean(final RippleValue rv) throws RippleException {
        Literal l = castToLiteral(rv.toRDF(this).sesameValue());

        //TODO: is capitalization relevant? Can 'true' also be represented as '1'?
        URI datatype = l.getDatatype();
        return null != datatype
                && XMLSchema.BOOLEAN.equals(datatype)
                && l.getLabel().equals("true");
    }

    public NumericValue toNumericValue(final RippleValue rv)
            throws RippleException {
        if (rv instanceof NumericValue) {
            return (NumericValue) rv;
        } else {
            return new SesameNumericValue(rv.toRDF(this));
        }
    }

    public Date toDateValue(RippleValue v) throws RippleException {
        Literal l = castToLiteral(v.toRDF(this).sesameValue());

        XMLGregorianCalendar c = l.calendarValue();
        return c.toGregorianCalendar().getTime();
    }

    // TODO: this method is incomplete
    public String toString(final RippleValue v) throws RippleException {
        if (v instanceof RDFValue) {
            Value r = ((RDFValue) v).sesameValue();

            if (r instanceof Literal) {
                return ((Literal) r).getLabel();
            } else {
                return r.toString();
            }
        } else {
            return v.toString();
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    public void add(final RippleValue subj, final RippleValue pred, final RippleValue obj, RippleValue... contexts)
            throws RippleException {
        Value subjValue = subj.toRDF(this).sesameValue();
        Value predValue = pred.toRDF(this).sesameValue();
        Value objValue = obj.toRDF(this).sesameValue();

        if (!(subjValue instanceof Resource)
                || !(predValue instanceof URI)) {
            return;
        }

        try {
            // Trick to be able to iterate through contexts even if none are given
            if (0 == contexts.length) {
                sailConnection.addStatement(
                        (Resource) subjValue, (URI) predValue, objValue);
            } else {
                for (RippleValue context : contexts) {
                    Value contextValue;

                    if (null == context) {
                        contextValue = null;
                    } else {
                        contextValue = context.toRDF(this).sesameValue();

                        // rdf:nil is a special case -- as a analysis name in Ripple, it
                        // actually represents the null analysis.
                        if (contextValue.equals(RDF.NIL)) {
                            contextValue = null;
                        } else if (!(contextValue instanceof Resource)) {
                            return;
                        }
                    }

                    sailConnection.addStatement(
                            (Resource) subjValue, (URI) predValue, objValue, (Resource) contextValue);
                }
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException(e);
        } catch (SailException e) {
            reset(true);
            throw new RippleException(e);
        }
    }

    public void remove(final RippleValue subj, final RippleValue pred, final RippleValue obj, final RippleValue... contexts)
            throws RippleException {
        Value subjValue, predValue, objValue;

        if (null == subj) {
            subjValue = null;
        } else {
            subjValue = subj.toRDF(this).sesameValue();
            if (!(subjValue instanceof Resource)) {
                return;
            }
        }

        if (null == pred) {
            predValue = null;
        } else {
            predValue = pred.toRDF(this).sesameValue();
            if (!(predValue instanceof URI)) {
                return;
            }
        }

        if (null == obj) {
            objValue = null;
        } else {
            objValue = obj.toRDF(this).sesameValue();
        }

        try {
            // Trick to be able to iterate through contexts even if none are given
            if (0 == contexts.length) {
                sailConnection.removeStatements(
                        (Resource) subjValue, (URI) predValue, objValue);
            } else {
                for (RippleValue context : contexts) {
                    Value contextValue;

                    if (null == context) {
                        contextValue = null;
                    } else {
                        contextValue = context.toRDF(this).sesameValue();

                        // rdf:nil is a special case -- as a analysis name in Ripple, it
                        // actually represents the null analysis.
                        if (contextValue.equals(RDF.NIL)) {
                            contextValue = null;
                        } else if (!(contextValue instanceof Resource)) {
                            return;
                        }
                    }

                    sailConnection.removeStatements(
                            (Resource) subjValue, (URI) predValue, objValue, (Resource) contextValue);
                }
            }
        } catch (SailReadOnlyException e) {
            handleSailReadOnlyException(e);
        } catch (SailException e) {
            reset(true);
            throw new RippleException(e);
        }
    }

    // Note: this method is no longer in the ModelConnection API
    public long countStatements(final Resource... contexts)
            throws RippleException {
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

    ////////////////////////////////////////////////////////////////////////////

    public Comparator<RippleValue> getComparator() {
        return comparator;
    }

    ////////////////////////////////////////////////////////////////////////////

    public RDFValue uriValue(final String s) throws RippleException {
        try {
// return canonicalize(valueFactory.createURI(s));
            // Note: do NOT automatically canonicalize values.  Sometimes one needs the original URI (e.g. so as to
            // remove statements), and not the native object it maps to.
            return new RDFValue(valueFactory.createURI(s));
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    public RippleValue canonicalValue(final RDFValue v) {
        return model.specialValues.get(v);
    }

    public RDFValue plainValue(final String s) throws RippleException {
        try {
            return new RDFValue(
                    valueFactory.createLiteral(s));
//                    valueFactory.createLiteral( s, XMLSchema.STRING ) );
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    public RDFValue languageTaggedValue(final String s, final String language)
            throws RippleException {
        try {
            return new RDFValue(
                    valueFactory.createLiteral(s, language));
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    public RDFValue typedValue(final String s, final URI dataType)
            throws RippleException {
        try {
            return new RDFValue(
                    valueFactory.createLiteral(s, dataType));
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    public RDFValue booleanValue(final boolean b)
            throws RippleException {
        try {
            return new RDFValue(
                    valueFactory.createLiteral("" + b, XMLSchema.BOOLEAN));
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    public NumericValue numericValue(final int i) throws RippleException {
        return new SesameNumericValue(i);
    }

    public NumericValue numericValue(final long l) throws RippleException {
        return new SesameNumericValue(l);
    }

    public NumericValue numericValue(final double d) throws RippleException {
        return new SesameNumericValue(d);
    }

    public NumericValue numericValue(final BigDecimal b) throws RippleException {
        return new SesameNumericValue(b);
    }

    ////////////////////////////////////////////////////////////////////////////

    public void setNamespace(final String prefix, final String ns, final boolean override)
            throws RippleException {
        //LOGGER.info( "### setting namespace: '" + prefix + "' to " + ns );
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
            handleSailReadOnlyException(e);
        } catch (Throwable t) {
            reset(true);
            throw new RippleException(t);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private class QueryTask extends Task {
        private final StatementPatternQuery query;
        private Sink<RippleValue> sink;

        public QueryTask(final StatementPatternQuery query, final Sink<RippleValue> sink) {
            this.query = query;
            this.sink = sink;
        }

        public void executeProtected() throws RippleException {
            query(query, sink, false);
        }

        protected void stopProtected() {
            synchronized (query) {
                sink = new NullSink<RippleValue>();
            }
        }
    }

    public void query(final StatementPatternQuery query,
                      final Sink<RippleValue> sink,
                      final boolean asynchronous) throws RippleException {
        if (asynchronous) {
            QueryTask task = new QueryTask(query, sink);
            taskSet.add(task);
        } else {
            GetStatementsQuery sesameQuery;

            try {
                sesameQuery = new GetStatementsQuery(query, this);
            } catch (GetStatementsQuery.InvalidQueryException e) {
                LOGGER.debug("invalid query: " + e.getMessage());
                return;
            }

            Sink<Value> valueSink = new Sink<Value>() {
                public void put(final Value val) throws RippleException {
                    //System.out.println("got value: " + val);
                    sink.put(canonicalValue(new RDFValue(val)));
                }
            };

            try {
                sesameQuery.getValues(sailConnection, valueSink);
            } catch (RippleException e) {
                reset(true);
                throw e;
            }
            //getStatements( query.subject, query.predicate, query.object, stSink, query.includeInferred );
        }
    }

    public Source<Namespace> getNamespaces() throws RippleException {
        Collector<Namespace> results = new Collector<Namespace>();
        Source<Namespace> source;

        try {
            source = new CloseableIterationSource<Namespace, SailException>(
                    (CloseableIteration<Namespace, SailException>) sailConnection.getNamespaces());
        } catch (SailException e) {
            throw new RippleException(e);
        }

        source.writeTo(results);

        return results;
    }

    //FIXME: Statements should be absent from the ModelConnection API
    public void getStatements(final RDFValue subj,
                              final RDFValue pred,
                              final RDFValue obj,
                              final Sink<Statement> sink)
            throws RippleException {
        Value rdfSubj = (null == subj) ? null : subj.sesameValue();
        Value rdfPred = (null == pred) ? null : pred.sesameValue();
        Value rdfObj = (null == obj) ? null : obj.sesameValue();

        if ((null == rdfSubj || rdfSubj instanceof Resource)
                && (null == rdfPred || rdfPred instanceof URI)) {
            // Note: we must collect results in a buffer before putting anything
            //       into the sink, as inefficient as that is, because otherwise
            //       we might end up opening another RepositoryResult before
            //       the one below closes, which currently causes Sesame to
            //       deadlock.  Even using a separate RepositoryConnection for
            //       each RepositoryResult doesn't seem to help.
            Buffer<Statement> buffer = new Buffer<Statement>(sink);
            CloseableIteration<? extends Statement, SailException> stmtIter = null;

            //TODO: use CloseableIterationSource

            // Perform the query and collect results.
            try {
                //synchronized ( model )
                //{
                stmtIter = sailConnection.getStatements(
                        (Resource) rdfSubj, (URI) rdfPred, rdfObj, false);
                //stmtIter.enableDuplicateFilter();

                while (stmtIter.hasNext()) {
                    Statement st = stmtIter.next();
                    try {
                        buffer.put(st);
                    } catch (RippleException e) {
                        // Soft fail
                        e.logError();
                    }
                }

                stmtIter.close();
                //}
            } catch (Throwable t) {
                try {
                    if (null != stmtIter) {
                        stmtIter.close();
                    }
                } catch (Throwable t2) {
                    t2.printStackTrace(System.err);
                    System.exit(1);
                }

                reset(true);
                throw new RippleException(t);
            }

            buffer.flush();
        }
    }

    public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(final String query)
            throws RippleException {
        SPARQLParser parser = new SPARQLParser();

        boolean useInference = false;
        // FIXME
        String baseURI = "http://example.org/bogusBaseURI/";

        ParsedQuery pq;
        try {
            pq = parser.parseQuery(query, baseURI);
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

    ////////////////////////////////////////////////////////////////////////////

    public Source<RippleValue> getContexts()
            throws RippleException {
        return new Source<RippleValue>() {
            public void writeTo(Sink<RippleValue> sink) throws RippleException {
                try {
                    CloseableIteration<? extends Resource, SailException> iter
                            = sailConnection.getContextIDs();

                    while (iter.hasNext()) {
                        sink.put(new RDFValue(iter.next()));
                    }

                    iter.close();
                } catch (SailException e) {
                    throw new RippleException(e);
                }
            }
        };
    }

    private void handleSailReadOnlyException(final SailReadOnlyException e) {
        // For now, ignore these exceptions.
    }

    public boolean internalize(final RippleList list) throws RippleException {
        Collector<Statement> buffer = new Collector<Statement>();

        // Handle circular lists (in the unlikely event that some implementation allows them) sanely.
        // TODO: handle list containment cycles (e.g. list containing a list containing the original list) as well.  These are actually more likely than circular lists.
        Set<RDFValue> alreadyInterned = new HashSet<RDFValue>();

        RippleList cur = list;
        RDFValue id = cur.toRDF(this);
        while (!cur.isNil()) {
            if (alreadyInterned.contains(id)) {
                break;
            } else {
                alreadyInterned.add(id);
            }

            RDFValue firstRdf = cur.getFirst().toRDF(this);

            if (null == firstRdf) {
                System.err.println("list item has no RDF identity: " + cur.getFirst());
                return false;
            }

            if (cur.getFirst() instanceof RippleList) {
                internalize((RippleList) cur.getFirst());
            }

            RippleList rest = cur.getRest();
            RDFValue restRdf = rest.toRDF(this);

            buffer.put(
                    valueFactory.createStatement((Resource) id.sesameValue(), RDF.TYPE, RDF.LIST));
            buffer.put(
                    valueFactory.createStatement((Resource) id.sesameValue(), RDF.FIRST, firstRdf.sesameValue()));
            buffer.put(
                    valueFactory.createStatement((Resource) id.sesameValue(), RDF.REST, restRdf.sesameValue()));

            cur = rest;
            id = restRdf;
        }

        RDFImporter importer = new RDFImporter(this);
        buffer.writeTo(importer.statementSink());

        return true;
    }
}
