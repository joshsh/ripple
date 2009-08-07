package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class InferInContextTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/inferInContextTest/> ." );
        reduce( "ex:ClassA rdf:type rdfs:Class ex:ctx1 assertInContext >> .\n"
                + "ex:ClassB rdf:type rdfs:Class ex:ctx1 assertInContext >>\n"
                + "    rdfs:subClassOf ex:ClassA ex:ctx1 assertInContext >> .\n"
                + "ex:a rdf:type ex:ClassB ex:ctx1 assertInContext >> .\n"
                + "ex:b rdf:type ex:ClassB ex:ctx2 assertInContext >> ." );

        // TODO: restore the two commented-out test cases below, which are the
        // only ones which distinguish analysis:inferInContext from
        // analysis:inContext.  Apparently, ForwardChainingRDFSInferencer does not
        // support the query patterns which would make analysis:inferInContext useful.
        
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