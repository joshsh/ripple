package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToMillisTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        // TODO: native syntax for xsd:long literals
        assertReducesTo("\"2008-04-01T11:34:36+06:00\"^^xsd:dateTime to-millis.", "\"1207028076000\"^^xsd:long");
    }
}