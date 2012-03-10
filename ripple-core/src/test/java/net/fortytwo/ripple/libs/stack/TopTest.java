package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TopTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("42 top.", "42");
        assertReducesTo("1 2 3 top.", "3");
        assertReducesTo("top.");
    }
}