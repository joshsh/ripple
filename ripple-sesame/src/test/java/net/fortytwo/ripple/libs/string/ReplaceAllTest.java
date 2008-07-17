package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ReplaceAllTest extends RippleTestCase
{
    public void testReplacementText() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"ss\" \".\" replaceAll >>", "\"Mi.i.ippi\"" );
        assertReducesTo( "\"Mississippi\" \"ss\" \"th\" replaceAll >>", "\"Mithithippi\"" );
    }

    public void testQuantifierGreediness() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"s\" \".\" replaceAll >>", "\"Mi..i..ippi\"" );
        assertReducesTo( "\"Mississippi\" \"s*\" \".\" replaceAll >>", "\".M.i..i..i.p.p.i.\"" );

        assertReducesTo( "\"Mississippi\" \"i.*s\" \".\" replaceAll >>", "\"M.ippi\"" );
    }

    public void testOrderOfReplacement() throws Exception
    {
        // Potential infinite loop avoided by skipping past replacement text
        assertReducesTo( "\"Mississippi\" \"Miss\" \"aMiss\" replaceAll >>", "\"aMississippi\"" );

        // Apparent conflict avoided by skipping past replacement text
        assertReducesTo( "\"Mississippi\" \"issi\" \".\" replaceAll >>", "\"M.ssippi\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" \"one\" \"two\" replaceAll >>", "\"\"" );
        assertReducesTo( "\"Mississippi\" \"s\" \"\" replaceAll >>", "\"Miiippi\"" );
        assertReducesTo( "\"Mississippi\" \"\" \".\" replaceAll >>", "\".M.i.s.s.i.s.s.i.p.p.i.\"" );
    }
}