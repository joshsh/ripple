package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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
