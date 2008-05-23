package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ToLowerCaseTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"BrainF@#$ language\" toLowerCase >>", "\"brainf@#$ language\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" toLowerCase >>", "\"\"" );
    }
}