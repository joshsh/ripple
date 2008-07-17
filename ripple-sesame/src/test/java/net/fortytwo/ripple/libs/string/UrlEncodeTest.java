package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class UrlEncodeTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" urlEncode >>", "\"one\"" );
        assertReducesTo( "\"one two\" urlEncode >>", "\"one+two\"" );
        assertReducesTo( "\"one\\ntwo\" urlEncode >>", "\"one%0Atwo\"" );
        assertReducesTo( "\"(rdf:rest *>>)\" urlEncode >>", "\"%28rdf%3Arest+*%3E%3E%29\"" );
        assertReducesTo( "\"encode me!\" urlEncode >>", "\"encode+me%21\"" );
    }
}