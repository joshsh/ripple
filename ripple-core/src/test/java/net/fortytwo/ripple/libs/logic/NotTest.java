package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NotTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("true not.", "false");
        assertReducesTo("false not.", "true");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 not.", "true");
    }
}