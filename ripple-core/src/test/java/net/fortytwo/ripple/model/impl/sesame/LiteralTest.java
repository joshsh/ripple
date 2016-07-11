package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LiteralTest extends RippleTestCase {
    @Test
    public void testEscapedCharacters() throws Exception {
        assertReducesTo("\"\\\"\" length.", "1");
        assertReducesTo("\"\\\"\"^^xsd:string length.", "1");
    }
}
