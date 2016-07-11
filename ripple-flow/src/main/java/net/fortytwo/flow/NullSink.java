package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A sink which simply throws away all data passed into it.
 *
 * @param <T> the type of element handled by this sink
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NullSink<T> implements Sink<T> {
    public void accept(final T t) throws RippleException {
        // Do nothing.
    }
}
