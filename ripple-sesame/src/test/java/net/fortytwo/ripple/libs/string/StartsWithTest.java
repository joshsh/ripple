package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class StartsWithTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" \"one\" startsWith >>", "true" );
        assertReducesTo( "\"one\" \"onetwo\" startsWith >>", "false" );
        assertReducesTo( "\"one\" \"o\" startsWith >>", "true" );
        assertReducesTo( "\"nation\" \"nat\" startsWith >>", "true" );
        assertReducesTo( "\"nation\" \"tion\" startsWith >>", "false" );

        // Case sensitivity
        assertReducesTo( "\"one\" \"O\" startsWith >>", "false" );

        // Empty strings
        assertReducesTo( "\"\" \"\" startsWith >>", "true" );
        assertReducesTo( "\"one\" \"\" startsWith >>", "true" );
        assertReducesTo( "\"\" \"foo\" startsWith >>", "false" );
    }
}