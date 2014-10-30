package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * The next step in a data pipeline.
 * Data items which are passed into a <code>Sink</code> may be transformed or passed along verbatim
 * to downstream components, or even thrown away;
 * a Sink is a black box with respect to upstream components passing data into it.
 *
 * @param <T> the type of data being passed
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Sink<T> {
    /**
     * Passes a data item into the <code>Sink</code>
     *
     * @param t the data item being passed
     * @throws RippleException if a data handling error occors
     */
    void put(T t) throws RippleException;
}
