package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DistinctTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "(2 3) each. distinct.", "2", "3" );
        assertReducesTo( "(2 3 2) each. distinct.", "2", "3" );
        assertReducesTo( "(2 2) each. distinct.", "2" );

        assertReducesTo( "((1 2) (2 1) (1 2)) each. distinct.", "(1 2)", "(2 1)" );
    }

    public void testListEquivalence() throws Exception
    {
        assertReducesTo( "(() rdf:nil) each. distinct.", "()" );

        reduce("@prefix : <http://example.org/distinctTest/>");
        reduce("@list foo: 1 2 3");
        assertReducesTo( ":foo", "(1 2 3)" );
        assertReducesTo( ":foo rdf:rest.", "(2 3)", "(2 3)" );
    }

    public void testArity() throws Exception
    {
        // 'distinct' currently has an arity of exactly one, regardless of the
        // depth to which the stack should be reduced to determine equality
        // in a "transparent" fashion
        assertReducesTo( "(1 2 2) (1 2 dup.) both. apply. distinct.", "1 2 2" );
        assertReducesTo( "(1 1 2) (1 dup. 2) both. apply. distinct.", "1 1 2", "1 dup. 2" );
    }

    public void testListTransparency() throws Exception
    {
        // Lists are currently not transparent to 'distinct'
        assertReducesTo( "(2 2) (2 dup.) both. distinct.", "(2 2)", "(2 dup.)" );
    }
}
