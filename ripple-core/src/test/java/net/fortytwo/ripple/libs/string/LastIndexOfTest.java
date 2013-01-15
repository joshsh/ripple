package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LastIndexOfTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "\"one\" \"one\" lastIndexOf.", "0" );
        assertReducesTo( "\"one\" \"two\" lastIndexOf.", "-1" );
        assertReducesTo( "\"one\" \"n\" lastIndexOf.", "1" );
        assertReducesTo( "\"one\" \"ne\" lastIndexOf.", "1" );
        assertReducesTo( "\"banana\" \"na\" lastIndexOf.", "4" );
        assertReducesTo( "\"fish\" \"sandwich\" lastIndexOf.", "-1" );

        // Case sensitivity
        assertReducesTo( "\"one\" \"oNe\" lastIndexOf.", "-1" );

        // Empty strings
        assertReducesTo( "\"\" \"\" lastIndexOf.", "0" );
        assertReducesTo( "\"one\" \"\" lastIndexOf.", "3" );
        assertReducesTo( "\"\" \"one\" lastIndexOf.", "-1" );
    }
}
