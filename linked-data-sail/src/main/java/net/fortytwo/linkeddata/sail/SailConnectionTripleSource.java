package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConnectionTripleSource implements TripleSource {
    private static final Logger logger = Logger.getLogger(SailConnectionTripleSource.class.getName());
    private final SailConnection sailConnection;
    private final ValueFactory valueFactory;
    private final boolean includeInferred;

    public SailConnectionTripleSource(final SailConnection conn,
                                      final ValueFactory valueFactory,
                                      final boolean includeInferred) {
        sailConnection = conn;
        this.valueFactory = valueFactory;
        this.includeInferred = includeInferred;
    }

    public CloseableIteration<? extends Statement, QueryEvaluationException> getStatements(
            final Resource subj, final URI pred, final Value obj, final Resource... contexts) {
        try {
            return new QueryEvaluationIteration(
                    sailConnection.getStatements(subj, pred, obj, includeInferred, contexts));
        } catch (SailException e) {
            logger.log(Level.WARNING, "query evaluation failed", e);
            return new EmptyCloseableIteration<>();
        }
    }

    public ValueFactory getValueFactory() {
        return valueFactory;
    }

    private static class QueryEvaluationIteration implements CloseableIteration<Statement, QueryEvaluationException> {
        private final CloseableIteration<? extends Statement, SailException> baseIteration;

        public QueryEvaluationIteration(final CloseableIteration<? extends Statement, SailException> baseIteration) {
            this.baseIteration = baseIteration;
        }

        public void close() throws QueryEvaluationException {
            try {
                baseIteration.close();
            } catch (SailException e) {
                throw new QueryEvaluationException(e);
            }
        }

        public boolean hasNext() throws QueryEvaluationException {
            try {
                return baseIteration.hasNext();
            } catch (SailException e) {
                throw new QueryEvaluationException(e);
            }
        }

        public Statement next() throws QueryEvaluationException {
            try {
                return baseIteration.next();
            } catch (SailException e) {
                throw new QueryEvaluationException(e);
            }
        }

        public void remove() throws QueryEvaluationException {
            try {
                baseIteration.remove();
            } catch (SailException e) {
                throw new QueryEvaluationException(e);
            }
        }
    }

    private static class EmptyCloseableIteration<T, E extends Exception> implements CloseableIteration<T, E> {
        public void close() throws E {
        }

        public boolean hasNext() throws E {
            return false;
        }

        public T next() throws E {
            return null;
        }

        public void remove() throws E {
        }
    }
}
