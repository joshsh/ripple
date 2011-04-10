package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AtTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "(1 2 3) 1 at.", "1" );
        assertReducesTo( "(1 2 3) 2 at.", "2" );
        assertReducesTo( "(1 2 3) 3 at.", "3" );
    }

    public void testOutOfRange() throws Exception
    {
        assertReducesTo( "() 1 at." );
        assertReducesTo( "(1 2 3) 0 at." );
        assertReducesTo( "(1 2 3) 4 at." );
    }

    public void testRDFLists() throws Exception
    {
        assertReducesTo( "@redefine myList: 1 2 3.\n"
                + ":myList 2 at.", "2" );
    }

    public void testListTransparency() throws Exception
    {
        assertReducesTo( "(1 dup.) 2 at.", "dup" );
    }

    public void testVirtualLists() throws Exception
    {
        // Note: this is an odd consequence of opaque virtual lists.  The
        // virtual list here is (dup.), the first element of which is dup.
        assertReducesTo( "2 dup 1 at.", "2 dup" );
    }
}
