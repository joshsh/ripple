package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AryTest extends RippleTestCase
{
    public void testNormal() throws Exception
    {
        assertReducesTo( "2 3 dup dip.", "2 dup. 3" );
        assertReducesTo( "2 3 dup dip. 2 ary.", "2 2 3" );        

        assertReducesTo( "@relist recfunc: "
                + "rdf:first (rdf:rest. :recfunc.) both. "
                + "2 ary. apply.\n"
                + "(100 200 300) :recfunc.", "100", "200", "300" );
    }

    public void testInsufficientArity() throws Exception
    {
        // TODO: this can't actually be tested without a way of halting the infinite loop
        //assertReducesTo( "@relist badrecfunc: rdf:first (rdf:rest. :badrecfunc.) both. 1 ary. apply.."
        //       + "(1 2 3) :badrecfunc.", "1", "2", "3" );
    }

    public void testExcessiveArity() throws Exception
    {
        assertReducesTo( "@relist weirdrecfunc: rdf:first (rdf:rest. :weirdrecfunc.) both. 3 ary. apply. .\n"
                + "(1 2 3) :weirdrecfunc." );
        assertReducesTo( "100 200 3 ary." );
    }
}
