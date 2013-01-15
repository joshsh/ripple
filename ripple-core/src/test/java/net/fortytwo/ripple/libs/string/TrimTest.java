package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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
