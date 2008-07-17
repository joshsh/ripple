package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class SplitTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"one, two,three\" \",[ ]*\" split >>", "(\"one\" \"two\" \"three\")" );
        assertReducesTo( "\"Mississippi\" \"i\" split >>", "(\"M\" \"ss\" \"ss\" \"pp\")" );
        assertReducesTo( "\"Mississippi\" \"ss\" split >>", "(\"Mi\" \"i\" \"ippi\")" );
        assertReducesTo( "\"Mississippi\" \"Miss\" split >>", "(\"\" \"issippi\")" );
        assertReducesTo( "\"Mississippi\" \"ippi\" split >>", "(\"Mississ\")" );
    }

    public void testNomatch() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"mountain\" split >>", "(\"Mississippi\")" );
    }

    public void testQuantifierGreediness() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"i.*i\" split >>", "(\"M\")" );
    }

    public void testConflict() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"issi\" split >>", "(\"M\" \"ssippi\")" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"Mississippi\" \"\" split >>", "(\"\" \"M\" \"i\" \"s\" \"s\" \"i\" \"s\" \"s\" \"i\" \"p\" \"p\" \"i\")" );
        assertReducesTo( "\"\" \"mountain\" split >>", "(\"\")" );
        assertReducesTo( "\"\" \"\" split >>", "(\"\")" );  
    }
}