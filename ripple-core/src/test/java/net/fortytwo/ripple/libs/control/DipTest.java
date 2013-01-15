package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DipTest extends RippleTestCase
{
    public void testArity() throws Exception
    {
        assertReducesTo( "2 3 dup dip.", "2 dup. 3" );
        assertReducesTo( "10 100 add. 0 2 add. 1 2 add. dup dip. 2 ary.", "10 100 add. 2 2 3" );
    }

    public void testPrimitives() throws Exception
    {
        assertReducesTo( "2 3 dup dip. 2 ary.", "2 2 3" );
    }

    public void testLists() throws Exception
    {
        assertReducesTo( "() dip." );
        assertReducesTo( "3 2 () dip. 2 ary.", "3 2" );
        assertReducesTo( "3 2 rdf:nil dip. 2 ary.", "3 2" );

        assertReducesTo( "1 (2 3) dip. 2 ary.", "2 3 1" );
        assertReducesTo( "1 (2 dup.) dip. 2 ary.", "2 2 1" );
    }

    public void testNonprograms() throws Exception
    {
        assertReducesTo( "3 2 apply. 2 ary." );
        assertReducesTo( "3 1 1 both. dip. 2 ary." );
    }
}