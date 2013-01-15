package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DropTest extends RippleTestCase {

    public void testSimple() throws Exception {
        assertReducesTo("(1 2 3) 0 drop.", "(1 2 3)");
        assertReducesTo("(1 2 3 4) 2 drop.", "(3 4)");
        assertReducesTo("(1 2 3) 3 drop.", "()");
    }

    public void testOutOfRangeNumber() throws Exception {
        assertReducesTo("(1 2 3) -1 drop.");
        assertReducesTo("(1 2 3) 4 drop.");
    }
}
