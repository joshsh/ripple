package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToStringTest extends RippleTestCase {
    @Test
    public void testNumericValues() throws Exception {
        assertReducesTo("42 to-string.", "\"42\"^^xsd:string");
        assertReducesTo("1.4141e0 to-string.", "\"1.4141\"^^xsd:string");
    }
}