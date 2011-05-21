package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class GetTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        getTestURIMap().put( "http://example.org/getTest.txt", getClass().getResource( "getTest.txt" ).toString() );

        // FIXME: 'file' protocol is not supported by extras:get
        //assertReducesTo( "<http://example.org/getTest.txt> get.", "\"testing, one two three...\"" );
    }
}