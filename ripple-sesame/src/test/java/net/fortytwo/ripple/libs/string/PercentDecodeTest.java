package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class PercentDecodeTest extends RippleTestCase
{
    // Note: currently identical to UrlDecode test
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" percentDecode >>", "\"one\"" );
        assertReducesTo( "\"one+two\" percentDecode >>", "\"one two\"" );
        assertReducesTo( "\"one%20two\" percentDecode >>", "\"one two\"" );
        assertReducesTo( "\"one%0Atwo\" percentDecode >>", "\"one\\ntwo\"" );
        assertReducesTo( "\"%28rdf%3Arest+*%3E%3E%29\" percentDecode >>", "\"(rdf:rest *>>)\"" );
        assertReducesTo( "\"I%27m%20decoded%21\" percentDecode >>", "\"I'm decoded!\"" );
    }
}