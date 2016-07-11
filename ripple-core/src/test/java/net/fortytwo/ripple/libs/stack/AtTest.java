package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AtTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("(1 2 3) 1 at.", "1");
        assertReducesTo("(1 2 3) 2 at.", "2");
        assertReducesTo("(1 2 3) 3 at.", "3");
    }

    @Test
    public void testOutOfRange() throws Exception {
        assertReducesTo("() 1 at.");
        assertReducesTo("(1 2 3) 0 at.");
        assertReducesTo("(1 2 3) 4 at.");
    }

    @Test
    public void testRDFLists() throws Exception {
        assertReducesTo("@relist myList: 1 2 3\n"
                + ":myList 2 at.", "2");
    }

    @Test
    public void testListTransparency() throws Exception {
        assertReducesTo("(1 dup.) 2 at.", "dup");
    }

    @Test
    public void testVirtualLists() throws Exception {
        // Note: this is an odd consequence of opaque virtual lists.  The
        // virtual list here is (dup.), the first element of which is dup.
        assertReducesTo("2 dup 1 at.", "2 dup");
    }
}
