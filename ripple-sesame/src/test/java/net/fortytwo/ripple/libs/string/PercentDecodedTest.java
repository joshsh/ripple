package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class PercentDecodedTest extends RippleTestCase
{
    // Note: currently identical to UrlDecode test
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" percent-decoded.", "\"one\"" );
        assertReducesTo( "\"one+two\" percent-decoded.", "\"one two\"" );
        assertReducesTo( "\"one%20two\" percent-decoded.", "\"one two\"" );
        assertReducesTo( "\"one%0Atwo\" percent-decoded.", "\"one\\ntwo\"" );
        assertReducesTo( "\"%28rdf%3Arest+*%3E%3E%29\" percent-decoded.", "\"(rdf:rest *>>)\"" );
        assertReducesTo( "\"I%27m%20decoded%21\" percent-decoded.", "\"I'm decoded!\"" );
    }
}
