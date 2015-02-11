package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ChoiceTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("true 1 2 choice.", "1");
        assertReducesTo("false 1 2 choice.", "2");
        assertReducesTo("42 1 2 choice.", "2");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 1 2 choice.", "2");
    }
}