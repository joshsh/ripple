package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class InferTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/inferTest/> ." );
        reduce( "ex:ClassA rdf:type rdfs:Class assert >> .\n"
                + "ex:ClassB rdf:type rdfs:Class assert >>\n"
                + "    rdfs:subClassOf ex:ClassA assert >> .\n"
                + "ex:a rdf:type ex:ClassB assert >> ." );

        assertReducesTo( "ex:a rdf:type >>", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type infer >>", "ex:ClassB", "ex:ClassA", "rdfs:Resource" );
        
        assertReducesTo( "ex:ClassB rdf:type infer <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type infer <<", "ex:a" );

        // Note: the full set of RDFS entailment rules is not within the scope
        // of this test.  Here, we're only interested in the two possible
        // application patterns (forward and backward) of the primitive.
    }
}