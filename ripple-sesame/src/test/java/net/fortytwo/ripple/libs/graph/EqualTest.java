package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class EqualTest extends NewRippleTestCase
{
    public void testEqual() throws Exception
    {
        assertReducesTo( "42 42 equal >>", "true" );
        assertReducesTo( "42 6 9 mul >> equal >>", "false" );

        // Two numeric literals
        assertReducesTo( "\"42\"^^xsd:double \"42\"^^xsd:integer equal >>", "true" );
        assertReducesTo( "\"42\"^^xsd:int \"42\"^^xsd:integer equal >>", "false" );

        // One native number and one numeric literal
        assertReducesTo( "42 \"42\"^^xsd:integer equal >>", "true" );
        assertReducesTo( "\"42\"^^xsd:integer 42.0 equal >>", "true" );

        // Nested lists
        assertReducesTo( "(1 2 3) (1 2 3) equal >>", "true" );
        assertReducesTo( "(1 2 3) (3 2 1) equal >>", "false" );

        // Two URIs
        assertReducesTo( "rdfs:comment rdfs:comment equal >>", "true" );
        assertReducesTo( "rdfs:comment rdfs:label equal >>", "false" );

        // List transparency
        assertReducesTo( "(2 2) (2 dup >>) equal >>", "false" );

        // Empty lists
        assertReducesTo( "() () equal >>", "true" );
        assertReducesTo( "(1) () equal >>", "false" );
        assertReducesTo( "() (1) equal >>", "false" );
//        assertReducesTo( "() rdf:nil equal >>", "true" );
//        assertReducesTo( "rdf:nil () equal >>", "true" );
    }
}
