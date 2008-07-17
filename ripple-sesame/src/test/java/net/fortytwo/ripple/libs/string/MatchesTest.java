package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class MatchesTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" \"one\" matches >>", "true" );
        assertReducesTo( "\"one\" \"on+e*\" matches >>", "true" );
        assertReducesTo( "\"banana\" \"ba(na)*\" matches >>", "true" );
        assertReducesTo( "\"banana\" \"ba(na){2}\" matches >>", "true" );
        assertReducesTo( "\"banana\" \"ba(na){3}\" matches >>", "false" );
        assertReducesTo( "\"Mississippi\" \"M(i[ps]{2})*i\" matches >>", "true" );

        // Non-greedy
        assertReducesTo( "\"123A\" \"\\\\d*A\" matches >>", "true" );
        assertReducesTo( "\"123A\" \".*\" matches >>", "true" );
        assertReducesTo( "\"123A\" \".*A\" matches >>", "true" );
        assertReducesTo( "\"123A\" \".*2.*A\" matches >>", "true" );

        // Empty strings
        assertReducesTo( "\"\" \"\" matches >>", "true" );
        assertReducesTo( "\"\" \"Q*\" matches >>", "true" );
        assertReducesTo( "\"one\" \"\" matches >>", "false" );
    }
}