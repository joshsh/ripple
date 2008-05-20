package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class CatTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        // Simple native lists
        assertReducesTo( "(42) () cat >>", "(42)" );
        assertReducesTo( "() (42) cat >>", "(42)" );
        assertReducesTo( "() () cat >>", "()" );
        assertReducesTo( "(1 2) (3 4) cat >>", "(1 2 3 4)" );

        // RDF lists
        assertReducesTo( "(42) rdf:nil cat >>", "(42)" );
        assertReducesTo( "rdf:nil (42) cat >>", "(42)" );
        assertReducesTo( "rdf:nil rdf:nil cat >>", "()" );
        // TODO: test RDF lists other than rdf:nil

        // Program/list equivalence
        assertReducesTo( "42 42 cat >>", "(42 >> 42 >>)" );
        assertReducesTo( "42 42 cat >> >>" );
        assertReducesTo( "1 2 swap dup cat >> >>", "2 1 1" );
        assertReducesTo( "(1 (2 3)) apply cat >> >>", "1 2 3" );
    }
}