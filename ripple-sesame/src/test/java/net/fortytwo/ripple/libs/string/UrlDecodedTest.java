package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class UrlDecodedTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" urlDecode.", "\"one\"" );
        assertReducesTo( "\"one+two\" urlDecode.", "\"one two\"" );
        assertReducesTo( "\"one%20two\" urlDecode.", "\"one two\"" );
        assertReducesTo( "\"one%0Atwo\" urlDecode.", "\"one\\ntwo\"" );
        assertReducesTo( "\"%28rdf%3Arest+*%3E%3E%29\" urlDecode.", "\"(rdf:rest *>>)\"" );
        assertReducesTo( "\"I%27m%20decoded%21\" urlDecode.", "\"I'm decoded!\"" );
    }
}
