package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PercentEncodedTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("\"one\" percentEncode.", "\"one\"");
        assertReducesTo("\"one two\" percentEncode.", "\"one%20two\"");
        assertReducesTo("\"one\\ntwo\" percentEncode.", "\"one%0Atwo\"");
        assertReducesTo("\"(rdf:rest *>>)\" percentEncode.", "\"%28rdf%3Arest%20*%3E%3E%29\"");
        assertReducesTo("\"encode me!\" percentEncode.", "\"encode%20me%21\"");
    }
}
