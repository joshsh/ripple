package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class InverseTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        // Invert primitive functions
        assertReducesTo( "2 3 add inverse..", "-1" );
        assertReducesTo( "2 3 div inverse..", "6" );

        // Invert an RDF predicate
        // Note: the double result is a result of the quirky way in which
        // definitions are bound to lists: the list is first pushed into the
        // triple store, then the head node is copied.  So there are two lists
        // which have '137' as their first element.
        assertReducesTo( "@relist foobar: 137 69\n"
                + "136 1 add. rdf:first inverse...", "137 69", "137 69" );
    }
}