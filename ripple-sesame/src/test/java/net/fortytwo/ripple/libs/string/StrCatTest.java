package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class StrCatTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"wilde\" \"beest\" strCat >>", "\"wildebeest\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"wilde\" \"\" strCat >>", "\"wilde\"" );
        assertReducesTo( "\"\" \"beest\" strCat >>", "\"beest\"" );
        assertReducesTo( "\"\" \"\" strCat >>", "\"\"" );
    }
}