package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AddTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("2 2 add.", "4");
        assertReducesTo("2 -2.0 add.", "0");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertFalse(4 < Double.NaN);
        assertFalse(4 > Double.NaN);
        assertFalse(4 == Double.NaN);

        assertReducesTo("\"NaN\"^^xsd:double \"NaN\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double \"INF\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double \"-INF\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"NaN\"^^xsd:double 4 add.", "\"NaN\"^^xsd:double");

        assertReducesTo("\"INF\"^^xsd:double \"NaN\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double \"INF\"^^xsd:double add.", "\"INF\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double \"-INF\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double 4 add.", "\"INF\"^^xsd:double");

        assertReducesTo("\"-INF\"^^xsd:double \"NaN\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double \"INF\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double \"-INF\"^^xsd:double add.", "\"-INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double 4 add.", "\"-INF\"^^xsd:double");

        assertReducesTo("4 \"NaN\"^^xsd:double add.", "\"NaN\"^^xsd:double");
        assertReducesTo("4 \"INF\"^^xsd:double add.", "\"INF\"^^xsd:double");
        assertReducesTo("4 \"-INF\"^^xsd:double add.", "\"-INF\"^^xsd:double");
        assertReducesTo("4 4 add.", "8");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("2 5.0 add~.", "-3");
        assertReducesTo("2 0 add~.", "2");
    }
}