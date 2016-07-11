package net.fortytwo.flow.rdf;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CloseableIterationSource<T, E extends Exception> implements Source<T> {
    private CloseableIteration<T, E> iteration;

    public CloseableIterationSource(final CloseableIteration<T, E> iter) {
        iteration = iter;
    }

    public void writeTo(final Sink<T> sink) throws RippleException {
        if (null == iteration) {
            throw new IllegalStateException("CloseableIterationSource may only be written once");
        }

        try {
            try {
                while (iteration.hasNext()) {
                    sink.accept(iteration.next());
                }
            } finally {
                close();
            }
        } catch (RippleException e) {
            throw e;
        } catch (Exception e) {
            throw new RippleException(e);
        }
    }

    private void close() throws E {
        if (null != iteration) {
            try {
                iteration.close();
            } finally {
                iteration = null;
            }
        }
    }
}
