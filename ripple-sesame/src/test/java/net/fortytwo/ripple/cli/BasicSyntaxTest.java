package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * User: josh
 * Date: 4/8/11
 * Time: 11:59 PM
 */
public class BasicSyntaxTest extends RippleTestCase {
    public void testWhitespaceInLists() throws Exception {
        assertReducesTo("( )", "()");
        assertReducesTo("(42 )", "( 42)");
    }

    public void testWhitespaceBetweenListItems() throws Exception {
        assertReducesTo("2 3 add.", "5");
        assertReducesTo("2 3 add .", "5");

        // FIXME: restore
        //assertIllegal("2(3)");
        //assertIllegal("2 3add.");
        //assertIllegal("8 8 add.sqrt.");

        assertLegal(".");
        assertLegal("**");
        assertLegal("2.. 2");
        assertIllegal("2..2");
        assertIllegal("2 .2");
    }

    public void testPotentiallyAmbiguousSyntax() throws Exception {
        assertReducesTo("2 +3 add.", "5");
        assertReducesTo("2 +3", "2 3");

        assertLegal("2+ 3");

        assertIllegal("2+3");

        assertReducesTo("42 to-string.", "\"42\"^^xsd:string");
        // This is a gray area; the syntax is legal, but the apparent keywords are not defined.
        assertIllegal("42 to- string.");
        assertIllegal("42 to -string.");
    }
}
