package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AssertTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/assertTest/>." );
        assertReducesTo( "ex:a rdf:type >>" );

        reduce( "ex:a rdf:type ex:Example assert >>" );
        assertReducesTo( "ex:a rdf:type >>", "ex:Example" );

        // Asserted statements are not mutually exclusive.
        assertReducesTo( "ex:a rdf:type ex:Thing assert >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:Example", "ex:Thing" );

        assertReducesTo( "ex:b rdf:type () assert >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type >>", "rdf:nil" );

        assertReducesTo( "ex:b rdf:type (1 2) assert >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type >> rdf:type >>", "rdf:List" );
    }

    public void testLiteralObjects() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertTest/>." );

        assertReducesTo( "ex:a ex:specialNumer >>" );
        assertReducesTo( "ex:a ex:specialNumer 42 assert >>", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer >>", "42" );

        assertReducesTo( "ex:a rdfs:comment >>" );
        assertReducesTo( "ex:a rdfs:comment \"something\" assert >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment >>", "\"something\"" );

        assertReducesTo( "ex:a rdfs:label >>" );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string assert >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:label >>", "\"something\"^^xsd:string" );

    }

    public void testImpossibleStatements() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertTest/>." );

        assertReducesTo( "42 rdfs:comment \"foo\" assert >>", "42" );
        assertReducesTo( "42 rdfs:comment >>" );

        assertReducesTo( "ex:a 42 \"foo\" assert >>", "ex:a" );
        assertReducesTo( "ex:a 42 >>" );
    }
}