/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.linkeddata.LinkedDataCache;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.UpdateExpr;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailConnectionListener;
import org.openrdf.sail.SailException;

/**
 * A connection to a LinkedDataSail
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSailConnection implements NotifyingSailConnection {

    private final Sail baseSail;
    private final ValueFactory valueFactory;
    private final LinkedDataCache linkedDataCache;
    // Buffering input to the wrapped SailConnection avoids deadlocks.

    private boolean open = false;
    private SailConnection baseConnection;

    ////////////////////////////////////////////////////////////////////////////

    public synchronized void addConnectionListener(final SailConnectionListener listener) {
        if (baseConnection instanceof NotifyingSailConnection) {
            ((NotifyingSailConnection) baseConnection).addConnectionListener(listener);
        }
    }

    public void addStatement(final Resource subj,
                             final URI pred,
                             final Value obj,
                             final Resource... contexts) throws SailException {
        baseConnection.addStatement(subj, pred, obj, contexts);
    }

    public void clear(final Resource... contexts) throws SailException {
        baseConnection.clear(contexts);
    }

    public void clearNamespaces() throws SailException {
        baseConnection.clearNamespaces();
    }

    public synchronized void close() throws SailException {
        baseConnection.close();

        open = false;
    }

    public void commit() throws SailException {
        baseConnection.commit();
    }

    // Note: doesn't need to be synchronized, as it reduces to getStatements
    //       calls, which are thread-safe and return thread-safe objects.
    public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(
            final TupleExpr tupleExpr,
            final Dataset dataset,
            final BindingSet bindings,
            final boolean includeInferred)
            throws SailException {
        // Decompose queries into getStatements so we can dereference URIs.
        try {
            TripleSource tripleSource = new SailConnectionTripleSource(this, valueFactory, includeInferred);
            EvaluationStrategyImpl strategy = new EvaluationStrategyImpl(tripleSource, dataset);

            return strategy.evaluate(tupleExpr, bindings);
        } catch (QueryEvaluationException e) {
            throw new SailException(e);
        }
    }

    public void executeUpdate(UpdateExpr updateExpr, Dataset dataset, BindingSet bindingSet, boolean b) throws SailException {
        baseConnection.executeUpdate(updateExpr, dataset, bindingSet, b);
    }

    public CloseableIteration<? extends Resource, SailException> getContextIDs()
            throws SailException {
        return baseConnection.getContextIDs();
    }

    public String getNamespace(final String prefix)
            throws SailException {
        return baseConnection.getNamespace(prefix);
    }

    public CloseableIteration<? extends Namespace, SailException> getNamespaces()
            throws SailException {
        return baseConnection.getNamespaces();
    }

    // Note: not synchronized, on account of URI dereferencing
    public CloseableIteration<? extends Statement, SailException> getStatements(
            final Resource subj,
            final URI pred,
            final Value obj,
            final boolean includeInferred,
            final Resource... contexts) throws SailException {

        // Pull in any data needed for this query.
        extendClosureToStatement(subj, pred, obj, contexts);

        // Now match the triple pattern.
        return baseConnection.getStatements(subj, pred, obj, includeInferred, contexts);
    }

    public boolean isOpen() throws SailException {
        return open;
    }

    public synchronized void removeConnectionListener(final SailConnectionListener listener) {
        if (baseConnection instanceof NotifyingSailConnection) {
            ((NotifyingSailConnection) baseConnection).removeConnectionListener(listener);
        }
    }

    public void removeNamespace(final String prefix)
            throws SailException {
        baseConnection.removeNamespace(prefix);
    }

    public void removeStatements(final Resource subj,
                                 final URI pred,
                                 final Value obj,
                                 final Resource... context) throws SailException {
        baseConnection.removeStatements(subj, pred, obj, context);
    }

    public void rollback() throws SailException {
        baseConnection.rollback();
    }

    public void setNamespace(final String prefix, final String name)
            throws SailException {
        baseConnection.setNamespace(prefix, name);
    }

    public long size(final Resource... contexts) throws SailException {
        return baseConnection.size(contexts);
    }

    ////////////////////////////////////////////////////////////////////////////

    LinkedDataSailConnection(final Sail baseSail,
                             final LinkedDataCache linkedDataCache)
            throws SailException {
        this.baseSail = baseSail;
        this.linkedDataCache = linkedDataCache;

        // Inherit the local store's ValueFactory
        valueFactory = baseSail.getValueFactory();

        openLocalStoreConnection();

        open = true;
    }

    ////////////////////////////////////////////////////////////////////////////

    private void openLocalStoreConnection()
            throws SailException {
        synchronized (baseSail) {
            baseConnection = baseSail.getConnection();
        }
    }

    private void retrieveUri(final URI uri) {
        try {
            linkedDataCache.retrieveUri(uri, baseConnection);
        } catch (RippleException e) {
            e.logError();
        }
    }

    private void extendClosureToStatement(final Resource subj,
                                          final URI pred,
                                          final Value obj,
                                          final Resource... contexts) throws SailException {
        if (linkedDataCache.getDereferenceSubjects() && null != subj && subj instanceof URI) {
            retrieveUri((URI) subj);
        }

        if (linkedDataCache.getDereferencePredicates() && null != pred) {
            retrieveUri(pred);
        }

        if (linkedDataCache.getDereferenceObjects() && null != obj && obj instanceof URI) {
            retrieveUri((URI) obj);
        }

        if (linkedDataCache.getDereferenceContexts()) {
            for (Resource ctx : contexts) {
                if (null != ctx && ctx instanceof URI) {
                    retrieveUri((URI) ctx);
                }
            }
        }
    }
}

