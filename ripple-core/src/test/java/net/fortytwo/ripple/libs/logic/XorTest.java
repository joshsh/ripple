package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class XorTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("true true xor.", "false");
        assertReducesTo("true false xor.", "true");
        assertReducesTo("false true xor.", "true");
        assertReducesTo("false false xor.", "false");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("true 42 xor.", "true");
        assertReducesTo("false 42 xor.", "false");
        assertReducesTo("42 true xor.", "true");
        assertReducesTo("42 false xor.", "false");
        assertReducesTo("42 42 xor.", "false");
    }

    public void testInverse() throws Exception {
        assertReducesTo("true xor~.", "true false", "false true");
        assertReducesTo("false xor~.", "false false");
    }
}