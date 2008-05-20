package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ConsTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        // Simple native lists
        assertReducesTo( "42 () cons >>", "(42)" );
        assertReducesTo( "1 (2 3) cons >>", "(1 2 3)" );

        // RDF lists
        assertReducesTo( "42 rdf:nil cons >>", "(42)" );
        // TODO: test RDF lists other than rdf:nil

        // Program/list equivalence
        assertReducesTo( "42 42 cons >>", "(42 42 >>)" );
        assertReducesTo( "42 42 cons >> >>" );
        assertReducesTo( "42 dup cons >> >>", "42 42" );

        // Misc. odd cases
        assertReducesTo( "() () cons >>", "(())" );
    }
}