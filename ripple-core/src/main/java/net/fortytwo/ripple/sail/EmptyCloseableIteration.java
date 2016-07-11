package net.fortytwo.ripple.sail;

import info.aduna.iteration.CloseableIteration;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class EmptyCloseableIteration<T, E extends Exception> implements CloseableIteration<T, E> {
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
