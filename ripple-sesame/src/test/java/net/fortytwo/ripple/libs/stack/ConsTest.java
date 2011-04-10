package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ConsTest extends RippleTestCase
{
    public void testSimpleNativeLists() throws Exception
    {
        assertReducesTo( "42 () cons.", "(42)" );
        assertReducesTo( "1 (2 3) cons.", "(1 2 3)" );
    }

    public void testRDFLists() throws Exception
    {
        assertReducesTo( "42 rdf:nil cons.", "(42)" );
        assertReducesTo( "@redefine myList: 1 2 3\n"
                + "0 :myList cons.", "(0 1 2 3)" );
    }

    public void testProgramListEquivalence() throws Exception
    {
        assertReducesTo( "42 42 cons.", "(42 42.)" );
        assertReducesTo( "42 42 cons.." );
        assertReducesTo( "42 dup cons..", "42 42" );
    }

    public void testMisc() throws Exception
    {
        assertReducesTo( "() () cons.", "(())" );
    }
}
