package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RequireTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("2 3 (add. 5 equal.) require.", "2 3");
        assertReducesTo("2 3 (add. 6 equal.) require.");
        assertReducesTo("(1 2 3 4) each. (2 mod. 0 equal.) require.", "2", "4");

        assertReducesTo("true id require.", "true");
        assertReducesTo("false id require.");
        assertReducesTo("false not require.", "false");
    }

    public void testArity() throws Exception {
        // Currently 'require' demands exactly two arguments, regardless of the
        // arity of the criterion function.
        assertReducesTo("2 2 1 add. (add. 5 equal.) require.", "2 3");
        assertReducesTo("1 1 add. 3 (add. 5 equal.) require.", "1 1 add. 3");
    }

    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 id require.");
        assertReducesTo("42 not require.", "42");
    }
}
