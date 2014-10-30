package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class FoldTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("(1 2 3) 0 add fold.", "6");
    }

    public void testEmptyList() throws Exception {
        assertReducesTo("() 42 add fold.", "42");
    }

    public void testOrder() throws Exception {
        assertReducesTo("(1 2 3) \"testing\" (toString. strCat.) fold.",
                "\"testing123\"^^<http://www.w3.org/2001/XMLSchema#string>");
    }
}
