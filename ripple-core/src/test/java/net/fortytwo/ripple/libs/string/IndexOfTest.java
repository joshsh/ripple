package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class IndexOfTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" \"one\" indexOf.", "0" );
        assertReducesTo( "\"one\" \"two\" indexOf.", "-1" );
        assertReducesTo( "\"one\" \"n\" indexOf.", "1" );
        assertReducesTo( "\"one\" \"ne\" indexOf.", "1" );
        assertReducesTo( "\"banana\" \"na\" indexOf.", "2" );
        assertReducesTo( "\"swell\" \"we\" indexOf.", "1" );
        assertReducesTo( "\"fish\" \"sandwich\" indexOf.", "-1" );

        // Case sensitivity
        assertReducesTo( "\"one\" \"oNe\" indexOf.", "-1" );

        // Empty strings
        assertReducesTo( "\"\" \"\" indexOf.", "0" );
        assertReducesTo( "\"one\" \"\" indexOf.", "0" );        
        assertReducesTo( "\"\" \"one\" indexOf.", "-1" );
    }
}
