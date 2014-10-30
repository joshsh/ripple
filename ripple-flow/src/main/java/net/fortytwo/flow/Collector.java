package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

import java.util.Iterator;

/**
 * A data collector which stores data items in the order it receives them.
 * <p/>
 * Note: while this class is not actually thread-safe, put() may safely be
 * called while writeTo() is in progress.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Collector<T> extends SimpleReadOnlyCollection<T> implements Sink<T>, Source<T> {
    private Node first, last;
    private int count;

    /**
     * Constructs a new collector
     */
    public Collector() {
        clear();
    }

    /**
     * Receives the next data item to be stored
     *
     * @param t the data item being passed
     * @throws RippleException if a data handling error occurs
     */
    public void put(final T t) throws RippleException {
        Node n = new Node(t, null);

        if (null == first) {
            first = n;
        } else {
            last.next = n;
        }

        last = n;
        count++;
    }

    /**
     * Pushes the collected items to the specified sink.
     * This operation does not cause this collector to become empty.
     *
     * @param sink the downstream sink to receive the data in this collector
     * @throws RippleException if a data handling error occurs
     */
    public void writeTo(final Sink<T> sink) throws RippleException {
        Node cur = first;
        while (null != cur) {
            // Any new items which are put() as a result of this call will
            // eventually be passed into the sink as well (even if clear()
            // is called).
            try {
                sink.put(cur.value);
            } catch (RippleException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            cur = cur.next;
        }
    }

    /**
     * @return the number of items in this collection
     */
    public int size() {
        return count;
    }

    /**
     * @return an iterator over the items in this collection
     */
    public Iterator<T> iterator() {
        return new NodeIterator(first);
    }

    @Override
    /**
     * Remove all items from this collection
     */
    public void clear() {
        first = null;
        count = 0;
    }

    private class Node {
        public T value;
        public Node next;

        public Node(final T value, final Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private class NodeIterator implements Iterator<T> {
        private Node cur;

        public NodeIterator(final Node first) {
            cur = first;
        }

        public boolean hasNext() {
            return null != cur;
        }

        public T next() {
            T value = cur.value;
            cur = cur.next;
            return value;
        }

        public void remove() {
        }
    }
}
