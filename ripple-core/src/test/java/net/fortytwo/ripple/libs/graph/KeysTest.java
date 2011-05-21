package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * User: josh
 * Date: 4/10/11
 * Time: 11:44 AM
 */
public class KeysTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("\"{\\\"foo\\\": true, \\\"bar\\\": [6, 9, 42]}\" to-json. keys.", "\"foo\"", "\"bar\"");
        assertReducesTo("() to-json.");
    }
}
