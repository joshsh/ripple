package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ConcatTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"wilde\" \"beest\" concat >>", "\"wildebeest\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"wilde\" \"\" concat >>", "\"wilde\"" );
        assertReducesTo( "\"\" \"beest\" concat >>", "\"beest\"" );
        assertReducesTo( "\"\" \"\" concat >>", "\"\"" );
    }
}