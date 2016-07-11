package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A Consumer variant which throws a RippleException.
 * It is used to create data pipelines which propagate an exception back to the caller
 * if processing fails at any level.
 *
 * @param <T> the type of data being passed
 * @author Joshua Shinavier (http://fortytwo.net)
 */
@FunctionalInterface
public interface Sink<T> {
    /**
     * Passes a data item into the <code>Sink</code>
     *
     * @param t the data item being passed
     * @throws RippleException if a data handling error occors
     */
    void accept(T t) throws RippleException;
}
