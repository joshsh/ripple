package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class OrTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("true true or.", "true");
        assertReducesTo("true false or.", "true");
        assertReducesTo("false true or.", "true");
        assertReducesTo("false false or.", "false");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("true 42 or.", "true");
        assertReducesTo("false 42 or.", "false");
        assertReducesTo("42 true or.", "true");
        assertReducesTo("42 false or.", "false");
        assertReducesTo("42 42 or.", "false");
    }

    public void testInverse() throws Exception {
        assertReducesTo("true or~.", "true true", "true false", "false true");
        assertReducesTo("false or~.", "false false");
    }
}