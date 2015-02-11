package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class WhileTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("0 (10 lt.) (1 add.) while.", "10");
        assertReducesTo("0 (10 gt.) (1 add.) while.", "0");

        // Even though the criterion (pop. true) removes the 42 from the head
        // of the stack, this effect is undone by stack restoration.
        assertReducesTo("42 (pop. false) (1 add.) while.", "42");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 (108) (1 add.) while.", "42");
    }
}