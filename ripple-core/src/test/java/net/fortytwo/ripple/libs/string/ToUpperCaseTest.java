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
        assertReducesTo( "\"BrainF@#$ language\" to-upper-case.", "\"BRAINF@#$ LANGUAGE\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" to-upper-case.", "\"\"" );
    }
}
