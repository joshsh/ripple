package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LiteralTest extends RippleTestCase {
    public void testEscapedCharacters() throws Exception {
        assertReducesTo("\"\\\"\" length.", "1");
        assertReducesTo("\"\\\"\"^^xsd:string length.", "1");
    }
}
