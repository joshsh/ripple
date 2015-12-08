package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.HashSet;
import java.util.Set;

/**
 * A "filter" pipeline which allows each distinct data item to pass only once.
 * Duplicates are filtered out rather than being passed to the downstream sink.
 *
 * @param <T> the type of element handled by this filter
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DistinctFilter<T> implements Sink<T> {
    private final Set<T> set = new HashSet<T>();
    private final Sink<T> sink;

    /**
     * Constructs a new filter
     *
     * @param sink the downstream sink to receive distinct data items
     */
    public DistinctFilter(final Sink<T> sink) {
        this.sink = sink;
    }

    /**
     * Receives the next data item passed into this filter
     *
     * @param t the data item being passed
     * @throws RippleException if a data handling error occurs
     */
    public void put(final T t) throws RippleException {
        if (set.add(t)) {
            sink.put(t);
        }
    }
}
