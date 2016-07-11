package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SinhTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("0 sinh.", "0");
        assertReducesTo("-1 sinh.", "-1.1752011936438014");
        assertReducesTo("1 sinh.", "1.1752011936438014");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double sinh.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double sinh.", "\"INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double sinh.", "\"-INF\"^^xsd:double");
    }

    @Ignore
    @Test
    public void testInverse() throws Exception {
        // TODO: implement inverse mapping
    }
}
