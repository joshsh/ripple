package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BasicSyntaxTest extends RippleTestCase {
    public void testWhitespaceInLists() throws Exception {
        assertReducesTo("( )", "()");
        assertReducesTo("(42 )", "( 42)");
    }

    public void testWhitespaceBetweenListItems() throws Exception {
        assertReducesTo("2 3 add.", "5");
        assertReducesTo("2 3 add .", "5");

        assertIllegal("2(3)");
        assertIllegal("2 3add.");
        assertIllegal("8 8 add.sqrt.");

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

    public void testPostPositionKeywords() throws Exception {
        assertReducesTo("1 2 3 = x", "1 2 3");
        assertReducesTo("x", "(1 2 3)");
        assertReducesTo("x.", "1 2 3");
        assertReducesTo("10 x. add.", "10 1 5");

        assertReducesTo("4 sqrt.\n=y", "2", "-2");
        assertReducesTo("x. y. 2 ary.", "1 2 3 2", "1 2 3 -2");

        // Redefine x.
        assertReducesTo("42 = x", "42");
        assertReducesTo("x.", "42");
    }
}
