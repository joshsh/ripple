package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ContainsTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"ippi\" contains.", "true");
        assertReducesTo( "\"Mississippi\" \"ippi\" contains.", "true");
        assertReducesTo( "\"Mississippi\" \"hippie\" contains.", "false");
    }

    public void testCaseSensitivity() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"miss\" contains.", "false");
        assertReducesTo( "\"Mississippi\" \"Sissi\" contains.", "false");
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"\" contains.", "true");
        assertReducesTo( "\"\" \"ippi\" contains.", "false");
        assertReducesTo( "\"\" \"\" contains.", "true");
    }
}
