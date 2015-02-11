package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ConsTest extends RippleTestCase {
    public void testSimpleNativeLists() throws Exception {
        assertReducesTo("42 () cons.", "(42)");
        assertReducesTo("1 (2 3) cons.", "(1 2 3)");
    }

    public void testRDFLists() throws Exception {
        assertReducesTo("42 rdf:nil cons.", "(42)");
        assertReducesTo("@relist myList: 1 2 3\n"
                + "0 :myList cons.", "(0 1 2 3)");
    }

    public void testProgramListEquivalence() throws Exception {
        assertReducesTo("42 42 cons.", "(42 42.)");
        assertReducesTo("42 42 cons..");
        assertReducesTo("42 dup cons..", "42 42");
    }

    public void testMisc() throws Exception {
        assertReducesTo("() () cons.", "(())");
    }
}
