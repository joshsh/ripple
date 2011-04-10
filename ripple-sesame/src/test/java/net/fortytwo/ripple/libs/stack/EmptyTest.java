package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class EmptyTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "(1 2 3) empty >>", "false" );
        assertReducesTo( "() empty >>", "true" );
    }
    
    public void testRDFLists() throws Exception
    {
        assertReducesTo( "rdf:nil empty >>", "true" );

        reduce("@define list42: 1 2 3 .");
        assertReducesTo( ":list42 empty >>", "false" );
    }

    public void testImplicitLists() throws Exception
    {
        assertReducesTo( "42 empty >>", "false" );
        assertReducesTo( "42 dup cat >> empty >>", "false" );
    }
}