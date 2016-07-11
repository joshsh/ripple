package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.LinkedList;

/**
 * A <code>Sink</code> which maintains a history of items received during any given interval between calls to
 * its <code>advance</code> method.
 * It has a limited capacity, remembering only the latest <code>k</code> intervals.
 *
 * @param <T> the type of data being passed
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HistorySink<T> implements Sink<T> {
    private final int capacity;
    private final LinkedList<Collector<T>> history;

    private int len;
    private Collector<T> current;

    /**
     * Constructs a new history sink with a given capacity
     *
     * @param capacity the number of items this history sink may hold
     */
    public HistorySink(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }

        this.capacity = capacity;
        history = new LinkedList<>();

        len = 0;
    }

    /**
     * Advances to the next step in the history
     */
    public void advance() {
        current = new Collector<>();
        history.addFirst(current);

        len++;
        if (len > capacity) {
            history.removeLast();
        }
    }

    /**
     * Receives the next data item passed in.
     * The item goes into the current step in the history.
     *
     * @param t the data item being passed
     * @throws RippleException if a data handling error occurs
     */
    public void accept(final T t) throws RippleException {
        current.accept(t);
    }

    /**
     * Retrieves a data source for a given step in the history.
     *
     * @param index the index of the desired step in the history.
     *              The current step has an index of 0,
     *              while the previous step has an index of 1, etc.
     * @return a data source producing all data items collected in the given step
     */
    public Source<T> get(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        } else if (index >= history.size()) {
            return null;
        } else {
            return history.get(index);
        }
    }
}
