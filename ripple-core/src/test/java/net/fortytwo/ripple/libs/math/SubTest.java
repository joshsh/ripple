package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SubTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("2 2 sub.", "0");
        assertReducesTo("2 -2.0 sub.", "4");
        assertReducesTo("42.2 -1.3 sub.", "43.5");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double \"NaN\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double \"INF\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double \"-INF\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double 42 sub.", "\"NaN\"^^xsd:double");

        assertReducesTo("\"INF\"^^xsd:double \"NaN\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double \"INF\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        // TODO: this is strange
        assertReducesTo("\"INF\"^^xsd:double \"-INF\"^^xsd:double sub.", "\"INF\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double 42 sub.", "\"INF\"^^xsd:double");

        assertReducesTo("\"-INF\"^^xsd:double \"NaN\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double \"INF\"^^xsd:double sub.", "\"-INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double \"-INF\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double 42 sub.", "\"-INF\"^^xsd:double");

        assertReducesTo("42 \"NaN\"^^xsd:double sub.", "\"NaN\"^^xsd:double");
        assertReducesTo("42 \"INF\"^^xsd:double sub.", "\"-INF\"^^xsd:double");
        assertReducesTo("42 \"-INF\"^^xsd:double sub.", "\"INF\"^^xsd:double");
        assertReducesTo("42 42 sub.", "0");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("2 2 sub~.", "4");
        assertReducesTo("2 -2.0 sub~.", "0");
    }
}
