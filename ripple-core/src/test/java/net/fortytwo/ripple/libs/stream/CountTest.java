package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CountTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "1000 2000 both count.", "1000 2000 2" );
        assertReducesTo( "\"One, two, three stacks. Ah ah ah!\" (\",\" split. each.) count.",
                "\"One, two, three stacks. Ah ah ah!\" 3" );
    }

    public void testInsufficientArguments() throws Exception {
        assertReducesTo("count.");
    }

    public void testIneffectualMapping() throws Exception {
        assertReducesTo("1 2 count.", "1 0");
    }
}
