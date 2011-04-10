package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ToUpperCaseTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"BrainF@#$ language\" toUpperCase.", "\"BRAINF@#$ LANGUAGE\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" toUpperCase.", "\"\"" );
    }
}
