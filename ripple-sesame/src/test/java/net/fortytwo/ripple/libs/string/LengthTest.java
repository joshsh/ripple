package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class LengthTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" length >>", "3" );
        assertReducesTo( "\"\" length >>", "0" );
    }

    // FIXME: string functions are currently applicable to all literals,
    // whereas they should really be limited to xsd:string values.
    public void testNonStrings() throws Exception
    {
        assertReducesTo( "42 length >>", "2" );
    }
}