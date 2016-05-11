package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.linkeddata.LinkedDataCache;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.IRI;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.EvaluationStrategy;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.impl.SimpleEvaluationStrategy;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailConnectionListener;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.NotifyingSailConnectionBase;
import org.openrdf.sail.helpers.SailBase;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A connection to a LinkedDataSail
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSailConnection extends NotifyingSailConnectionBase {

    private static final Logger logger = Logger.getLogger(LinkedDataSailConnection.class.getName());

    private final ValueFactory valueFactory;
    private final LinkedDataCache linkedDataCache;

    private SailConnection baseConnection;

    public synchronized void addConnectionListener(final SailConnectionListener listener) {
        if (baseConnection instanceof NotifyingSailConnection) {
            ((NotifyingSailConnection) baseConnection).addConnectionListener(listener);
        }
    }

    protected void addStatementInternal(final Resource subj,
                                        final IRI pred,
                                        final Value obj,
                                        final Resource... contexts) throws SailException {
        baseConnection.addStatement(subj, pred, obj, contexts);
    }

    protected void clearInternal(final Resource... contexts) throws SailException {
        baseConnection.clear(contexts);
    }

    protected void clearNamespacesInternal() throws SailException {
        baseConnection.clearNamespaces();
    }

    protected synchronized void closeInternal() throws SailException {
        baseConnection.rollback();
        baseConnection.close();
    }

    protected void commitInternal() throws SailException {
        baseConnection.commit();
    }

    protected CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluateInternal(
            final TupleExpr tupleExpr,
            final Dataset dataset,
            final BindingSet bindings,
            final boolean includeInferred)
            throws SailException {
        // Decompose queries into getStatements operations so we can dereference URIs.
        try {
            TripleSource tripleSource = new SailConnectionTripleSource(this, valueFactory, includeInferred);
            EvaluationStrategy strategy = new SimpleEvaluationStrategy(tripleSource, dataset, null);

            return strategy.evaluate(tupleExpr, bindings);
        } catch (QueryEvaluationException e) {
            throw new SailException(e);
        }
    }

    protected CloseableIteration<? extends Resource, SailException> getContextIDsInternal()
            throws SailException {
        return baseConnection.getContextIDs();
    }

    protected String getNamespaceInternal(final String prefix)
            throws SailException {
        return baseConnection.getNamespace(prefix);
    }

    protected CloseableIteration<? extends Namespace, SailException> getNamespacesInternal()
            throws SailException {
        return baseConnection.getNamespaces();
    }

    protected CloseableIteration<? extends Statement, SailException> getStatementsInternal(
            final Resource subj,
            final IRI pred,
            final Value obj,
            final boolean includeInferred,
            final Resource... contexts) throws SailException {

        // Pull in any data needed for this query.
        extendClosureToStatement(subj, pred, obj, contexts);

        // Now match the triple pattern.
        return baseConnection.getStatements(subj, pred, obj, includeInferred, contexts);
    }

    public synchronized void removeConnectionListener(final SailConnectionListener listener) {
        if (baseConnection instanceof NotifyingSailConnection) {
            ((NotifyingSailConnection) baseConnection).removeConnectionListener(listener);
        }
    }

    protected void removeNamespaceInternal(final String prefix)
            throws SailException {
        baseConnection.removeNamespace(prefix);
    }

    protected void removeStatementsInternal(final Resource subj,
                                            final IRI pred,
                                            final Value obj,
                                            final Resource... context) throws SailException {
        baseConnection.removeStatements(subj, pred, obj, context);
    }

    protected void rollbackInternal() throws SailException {
        baseConnection.rollback();
        //baseConnection.begin();
    }

    protected void setNamespaceInternal(final String prefix, final String name)
            throws SailException {
        baseConnection.setNamespace(prefix, name);
    }

    protected long sizeInternal(final Resource... contexts) throws SailException {
        return baseConnection.size(contexts);
    }

    protected void startTransactionInternal() throws SailException {
        baseConnection.begin();
    }

    LinkedDataSailConnection(final SailBase sail,
                             final Sail baseSail,
                             final LinkedDataCache linkedDataCache) throws SailException {
        super(sail);
        this.linkedDataCache = linkedDataCache;

        // Inherit the local store's ValueFactory
        valueFactory = baseSail.getValueFactory();

        baseConnection = baseSail.getConnection();
    }

    private void retrieveUri(final IRI uri) {
        try {
            linkedDataCache.retrieve(uri, baseConnection);
        } catch (RippleException e) {
            logger.log(Level.SEVERE, "failed to retrieve URI", e);
        }
    }

    private void extendClosureToStatement(final Resource subj,
                                          final IRI pred,
                                          final Value obj,
                                          final Resource... contexts) throws SailException {
        if (linkedDataCache.getDereferenceSubjects() && null != subj && subj instanceof IRI) {
            retrieveUri((IRI) subj);
        }

        if (linkedDataCache.getDereferencePredicates() && null != pred) {
            retrieveUri(pred);
        }

        if (linkedDataCache.getDereferenceObjects() && null != obj && obj instanceof IRI) {
            retrieveUri((IRI) obj);
        }

        if (linkedDataCache.getDereferenceContexts()) {
            for (Resource ctx : contexts) {
                if (null != ctx && ctx instanceof IRI) {
                    retrieveUri((IRI) ctx);
                }
            }
        }
    }
}

