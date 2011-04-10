package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class InContextTest extends RippleTestCase
{
    // Note: the tests for assertIn and denyIn also test inContext.

    public void testSimple() throws Exception
    {
        // Identical statements in two different contexts.
        reduce("@prefix ex: <http://example.org/inContextTest1/>.");
        reduce("ex:a rdf:type ex:ClassA ex:ctx1 assertInContext >>");
        reduce("ex:a rdf:type ex:ClassA ex:ctx2 assertInContext >>");
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >>", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext >>", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx3 inContext >>" );
        assertReducesTo( "ex:ClassA rdf:type <<", "ex:a", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx2 inContext <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx3 inContext <<" );

        // Statements with same subject in two different contexts.
        reduce("@prefix ex: <http://example.org/inContextTest2/>.");
        reduce("ex:a rdf:type ex:ClassA ex:ctx1 assertInContext >>");
        reduce("ex:a rdf:type ex:ClassB ex:ctx2 assertInContext >>");
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >>", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext >>", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx2 inContext <<" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 inContext <<" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx2 inContext <<", "ex:a" );

        // Statements with same object in two different statements.
        reduce("@prefix ex: <http://example.org/inContextTest3/>.");
        reduce("ex:a rdf:type ex:ClassA ex:ctx1 assertInContext >>");
        reduce("ex:b rdf:type ex:ClassA ex:ctx2 assertInContext >>");
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA" );
        assertReducesTo( "ex:b rdf:type >>", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >>", "ex:ClassA" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx2 inContext >>", "ex:ClassA" );
        assertReducesTo( "ex:ClassA rdf:type <<", "ex:a", "ex:b" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx2 inContext <<", "ex:b" );
    }
}