package net.fortytwo.ripple;

import junit.framework.TestCase;

import java.util.Comparator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ListMemoizerTest extends TestCase {

    public void testEmptyMemoizer() throws Exception {
        ListMemoizer<String, Integer> m = createMemoizer();

        assertNull(m.get(createList()));
        assertNull(m.get(createList("foo")));
        assertNull(m.get(createList("foo", "bar")));
    }

    public void testPaths() throws Exception {
        ListMemoizer<String, Integer> m = createMemoizer();

        m.put(createList("fox"), 0);
        assertEquals((Integer) 0, m.get(createList("fox")));

        m.put(createList("the", "quick"), 1);
        assertEquals((Integer) 1, m.get(createList("the", "quick")));
        assertNull(m.get(createList("the")));

        m.put(createList("brown"), 2);
        m.put(createList("the", "quick", "brown"), 3);
        assertEquals((Integer) 1, m.get(createList("the", "quick")));
        assertNull(m.get(createList("the")));
        assertEquals((Integer) 2, m.get(createList("brown")));
        assertEquals((Integer) 3, m.get(createList("the", "quick", "brown")));
    }

    public void testAsSetOfLists() throws Exception {
        ListMemoizer<String, Integer> m = createMemoizer();

        assertTrue(m.put(createList("foo"), 42));
        assertFalse(m.put(createList("foo"), 42));
        assertTrue(m.put(createList("foo", "bar"), 42));
        assertTrue(m.put(createList("one", "two", "three"), 42));
        assertTrue(m.put(createList("one"), 42));
        assertFalse(m.put(createList("one", "two", "three"), 42));
    }

    public void testRemove() throws Exception {
        ListMemoizer<String, Integer> m = createMemoizer();

        assertFalse(m.remove(createList("foo")));
        assertFalse(m.remove(createList("foo", "bar")));
        assertFalse(m.remove(createList()));

        m.put(createList("1", "2", "3", "4"), 0);
        m.put(createList("one", "two", "three"), 1);
        m.put(createList("a", "b"), 2);
        assertFalse(m.remove(createList("one")));
        assertFalse(m.remove(createList("one", "two")));
        assertEquals((Integer) 1, m.get(createList("one", "two", "three")));
        assertTrue(m.remove(createList("one", "two", "three")));
        assertNull(m.get(createList("one", "two", "three")));
        assertEquals((Integer) 0, m.get(createList("1", "2", "3", "4")));
        assertTrue(m.remove(createList("1", "2", "3", "4")));
        assertNull(m.get(createList("1", "2", "3", "4")));

        // Put previously removed items back in the list
        m.put(createList("one", "two", "three"), 42);
        assertEquals((Integer) 42, m.get(createList("one", "two", "three")));
    }

    private ListMemoizer<String, Integer> createMemoizer() {
        return new ListMemoizer<String, Integer>(new StringComparator());
    }

    private SimpleList<String> createList(final String... args) {
        SimpleList<String> l = new SimpleList<String>();

        for (String arg : args) {
            l = new SimpleList<String>(arg, l);
        }

        return l;
    }

    private class StringComparator implements Comparator<String> {
        public int compare(final String first,
                           final String second) {
            return first.compareTo(second);
        }
    }

    private class SimpleList<T> extends ListNode<T> {
        private final T first;
        private final SimpleList<T> rest;

        public SimpleList() {
            this.first = null;
            this.rest = null;
        }

        public SimpleList(final T first,
                          final SimpleList<T> rest) {
            this.first = first;
            this.rest = rest;
        }

        @Override
        public T getFirst() {
            return first;
        }

        @Override
        public ListNode<T> getRest() {
            return rest;
        }

        @Override
        public boolean isNil() {
            return first == null;
        }
    }
}
