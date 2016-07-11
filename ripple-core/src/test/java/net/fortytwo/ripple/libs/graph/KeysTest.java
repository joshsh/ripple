package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class KeysTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("\"{\\\"foo\\\": true, \\\"bar\\\": [6, 9, 42]}\" to-json. keys.", "\"foo\"", "\"bar\"");
        //assertIllegal("() to-json.");
        assertReducesTo("() to-json.");
    }
}
