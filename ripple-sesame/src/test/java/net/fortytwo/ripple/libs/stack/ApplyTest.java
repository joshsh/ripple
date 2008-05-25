package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ApplyTest extends NewRippleTestCase
{
    public void testPrimitives() throws Exception
    {
        assertReducesTo( "2 dup apply >>", "2 2" );
        assertReducesTo( "2 dup apply apply apply apply apply >>", "2 2" );        
    }

    public void testLists() throws Exception
    {
        assertReducesTo( "() apply >>" );
        assertReducesTo( "2 () apply >>", "2" );
        assertReducesTo( "2 rdf:nil apply >>", "2" );

        assertReducesTo( "1 (2 3) apply >>", "1 2 3" );
        assertReducesTo( "1 (2 dup >>) apply >>", "1 2 2" );
    }

    public void testNonprograms() throws Exception
    {
        assertReducesTo( "2 apply >>" );
        assertReducesTo( "1 1 both >>", "1", "1" );
    }
}