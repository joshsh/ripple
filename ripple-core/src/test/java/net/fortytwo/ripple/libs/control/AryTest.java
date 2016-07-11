package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AryTest extends RippleTestCase {
    @Test
    public void testNormal() throws Exception {
        assertReducesTo("2 3 dup dip.", "2 dup. 3");
        assertReducesTo("2 3 dup dip. 2 ary.", "2 2 3");

        assertReducesTo("@relist recfunc: "
                + "rdf:first (rdf:rest. :recfunc.) both. "
                + "2 ary. apply.\n"
                + "(100 200 300) :recfunc.", "100", "200", "300");
    }

    @Ignore
    @Test
    public void testInsufficientArity() throws Exception {
        // TODO: this can't actually be tested without a way of halting the infinite loop
        //assertReducesTo( "@relist badrecfunc: rdf:first (rdf:rest. :badrecfunc.) both. 1 ary. apply.."
        //       + "(1 2 3) :badrecfunc.", "1", "2", "3" );
    }

    @Test
    public void testExcessiveArity() throws Exception {
        assertReducesTo("@relist weirdrecfunc: rdf:first (rdf:rest. :weirdrecfunc.) both. 3 ary. apply. .\n"
                + "(1 2 3) :weirdrecfunc.");
        assertReducesTo("100 200 3 ary.");
    }
}
