package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class MapTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        // Note: 'map' is eager in that the values in the produced list are
        // fully reduced.

        // Mapping is a primitive
        assertReducesTo( "(1 2 3) neg map >>", "(-1 -2 -3)" );

        // Mapping is a list
        assertReducesTo( "(1 2 3) (10 add >>) map >>", "(11 12 13)" );

        // Empty list as argument, mapping
        assertReducesTo( "() neg map >>", "()" );
        assertReducesTo( "(1 2 3) () map >>", "(1 2 3)" );

        // Branching values
        assertReducesTo( "(0 1) sqrt map >>", "(0.0 1.0)", "(0.0 -1.0)" );
    }
}