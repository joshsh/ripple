package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class TrimTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "\"one two\" trim.", "\"one two\"" );
        assertReducesTo( "\" one two\" trim.", "\"one two\"" );
        assertReducesTo( "\"one two \" trim.", "\"one two\"" );
        assertReducesTo( "\"\\n\\tone two \\t\" trim.", "\"one two\"" );
    }

    public void testEmptyStrings() throws Exception
    {
        assertReducesTo( "\"\" trim.", "\"\"" );
    }
}
