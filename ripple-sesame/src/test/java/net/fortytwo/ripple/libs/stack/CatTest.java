package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class CatTest extends RippleTestCase
{
    public void testSimpleNativeLists() throws Exception
    {
        assertReducesTo( "(42) () cat.", "(42)" );
        assertReducesTo( "() (42) cat.", "(42)" );
        assertReducesTo( "() () cat.", "()" );
        assertReducesTo( "(1 2) (3 4) cat.", "(1 2 3 4)" );
    }

    public void testRDFLists() throws Exception
    {
        assertReducesTo( "(42) rdf:nil cat.", "(42)" );
        assertReducesTo( "rdf:nil (42) cat.", "(42)" );
        assertReducesTo( "rdf:nil rdf:nil cat.", "()" );

        assertReducesTo( "@redefine myList: 1 2 3\n"
                + "() :myList cat.", "(1 2 3)" );
        assertReducesTo( "@redefine myList: 1 2 3\n"
                + ":myList () cat.", "(1 2 3)" );
        assertReducesTo( "@redefine myList: 1 2 3\n"
                + "(4 5) :myList cat.", "(4 5 1 2 3)" );
        assertReducesTo( "@redefine myList: 1 2 3\n"
                + ":myList (4 5) cat.", "(1 2 3 4 5)" );
        assertReducesTo( "@redefine myList: 1 2 3\n"
                + ":myList :myList cat.", "(1 2 3 1 2 3)" );
    }

    public void testProgramListEquivalence() throws Exception
    {
        assertReducesTo( "42 42 cat.", "(42. 42.)" );
        assertReducesTo( "42 42 cat.." );
        assertReducesTo( "1 2 swap dup cat..", "2 1 1" );
        assertReducesTo( "(1 (2 3)) apply cat..", "1 2 3" );
    }
}
