package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class EndsWithTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" \"one\" endsWith >>", "true" );
        assertReducesTo( "\"one\" \"onetwo\" endsWith >>", "false" );
        assertReducesTo( "\"one\" \"e\" endsWith >>", "true" );
        assertReducesTo( "\"nation\" \"tion\" endsWith >>", "true" );
        assertReducesTo( "\"nation\" \"ism\" endsWith >>", "false" );
            
        // Case sensitivity
        assertReducesTo( "\"one\" \"E\" endsWith >>", "false" );

        // Empty strings
        assertReducesTo( "\"\" \"\" endsWith >>", "true" );
        assertReducesTo( "\"one\" \"\" endsWith >>", "true" );
        assertReducesTo( "\"\" \"foo\" endsWith >>", "false" );
    }
}