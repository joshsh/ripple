package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ScrapTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("scrap.");
        assertReducesTo("42 scrap.");
        assertReducesTo("(1 2 3) each. scrap.");
    }
}
