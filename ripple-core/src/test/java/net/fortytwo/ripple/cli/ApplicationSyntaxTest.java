package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ApplicationSyntaxTest extends RippleTestCase {
    public void testWhitespace() throws Exception {
        // White space is allowed between a quantifier and an application symbol.
        assertReducesTo("1 (10 add.){2}", "21");
        assertReducesTo("1 (10 add .) {2}", "21");
        assertReducesTo("(1 2 3) rdf:rest * rdf:first .", "1", "2", "3");
        assertReducesTo("(1 2 3) rdf:rest* rdf:first.", "1", "2", "3");
        assertReducesTo("(1 2 3) rdf:rest* rdf:first .", "1", "2", "3");
        assertReducesTo("2 (3 add.) ~ {1, 2}", "-1", "-4");
        assertReducesTo("2 (3 add.) ~ {1, 2}", "-1", "-4");
        assertReducesTo("2 (3 add.)~   \t {1, 2}", "-1", "-4");

        // White space within numeric and range quantifiers is ignored.
        assertReducesTo("2 (3 add.) ~ {1, \t2}", "-1", "-4");
        assertReducesTo("2 (3 add.) ~ { \t1,2}", "-1", "-4");
        assertReducesTo("0 (1 add.) {5}", "5");
        assertReducesTo("0 (1 add.) {\t5   }", "5");
    }
}