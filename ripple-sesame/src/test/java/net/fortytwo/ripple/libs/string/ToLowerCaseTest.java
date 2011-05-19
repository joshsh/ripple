package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ToLowerCaseTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"BrainF@#$ language\" to-lower-case.", "\"brainf@#$ language\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" to-lower-case.", "\"\"" );
    }
}
