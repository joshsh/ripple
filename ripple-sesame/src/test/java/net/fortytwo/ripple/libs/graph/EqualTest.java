package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

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
        assertReducesTo( "\"42\"^^<" + XMLSchema.DOUBLE + "> \"42\"^^<" + XMLSchema.INTEGER + "> equal >>", "true" );
        assertReducesTo( "\"42\"^^<" + XMLSchema.INT + "> \"42\"^^<" + XMLSchema.INTEGER + "> equal >>", "true" );

        // One native number and one numeric literal
        assertReducesTo( "42 \"42\"^^<" + XMLSchema.INT + "> equal >>", "true" );
        assertReducesTo( "\"42\"^^<" + XMLSchema.INTEGER + "> 42.0 equal >>", "true" );

        // Nested lists
        assertReducesTo( "(1 2 3) (1 2 3) equal >>", "true" );
        assertReducesTo( "(1 2 3) (3 2 1) equal >>", "false" );

        // Two URIs
        assertReducesTo( "<" + RDFS.COMMENT + "> <" + RDFS.COMMENT + "> equal >>", "true" );
        assertReducesTo( "<" + RDFS.COMMENT + "> <" + RDFS.LABEL + "> equal >>", "false" );

        // List transparency
        assertReducesTo( "(2 2) (2 dup >>) equal >>", "false" );

        // Empty lists
        assertReducesTo( "() () equal >>", "true" );
        assertReducesTo( "(1) () equal >>", "false" );
        assertReducesTo( "() (1) equal >>", "false" );
//        assertReducesTo( "() <" + RDF.NIL + "> equal >>", "true" );
//        assertReducesTo( "<" + RDF.NIL + "> () equal >>", "true" );
    }
}
