package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class MapTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        // Note: 'map' is eager in that the values in the produced list are
        // fully reduced.

        // Mapping is a primitive
        assertReducesTo( "(1 2 3) neg map.", "(-1 -2 -3)" );

        // Mapping is a list
        assertReducesTo( "(1 2 3) (10 add.) map.", "(11 12 13)" );

        // Empty list as argument, mapping
        assertReducesTo( "() neg map.", "()" );
        assertReducesTo( "(1 2 3) () map.", "(1 2 3)" );

        // Branching values are distributed
        assertReducesTo( "(0 1) sqrt map.", "(0.0 1.0)", "(0.0 -1.0)" );
    }
}
