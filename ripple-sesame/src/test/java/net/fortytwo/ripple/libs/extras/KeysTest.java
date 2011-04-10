package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * User: josh
 * Date: 4/10/11
 * Time: 11:44 AM
 */
public class KeysTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("\"{\\\"foo\\\": true, \\\"bar\\\": [6, 9, 42]}\" toJson. keys.", "\"foo\"", "\"bar\"");
        assertReducesTo("() toJson.");
    }
}
