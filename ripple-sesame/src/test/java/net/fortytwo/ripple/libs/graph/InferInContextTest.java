package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class InferInContextTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/inferInContextTest/> ." );
        reduce( "ex:ClassA rdf:type rdfs:Class ex:ctx1 assertIn >> .\n"
                + "ex:ClassB rdf:type rdfs:Class ex:ctx1 assertIn >>\n"
                + "    rdfs:subClassOf ex:ClassA ex:ctx1 assertIn >> .\n"
                + "ex:a rdf:type ex:ClassB ex:ctx1 assertIn >> .\n"
                + "ex:b rdf:type ex:ClassB ex:ctx2 assertIn >> ." );

        // TODO: restore the two commented-out test cases below, which are the
        // only ones which distinguish graph:inferInContext from
        // graph:inContext.  Apparently, ForwardChainingRDFSInferencer does not
        // support the query patterns which would make graph:inferInContext useful.
        
        assertReducesTo( "ex:a rdf:type ex:ctx1 inferInContext >>", "ex:ClassB" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inferInContext >>" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inferInContext >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx2 inferInContext >>", "ex:ClassB" );
//        assertReducesTo( "ex:a rdf:type ex:ctx1 inferInContext >>", "ex:ClassB", "ex:ClassA", "rdfs:Resource" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inferInContext >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inferInContext >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx2 inferInContext >>", "ex:ClassB" );

        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 inferInContext <<", "ex:a" );
//        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inferInContext <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx2 inferInContext <<", "ex:b" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx2 inferInContext <<" );

        // Note: the full set of RDFS entailment rules is not within the scope
        // of this test.  Here, we're only interested in the two possible
        // application patterns (forward and backward) of the primitive.
    }
}