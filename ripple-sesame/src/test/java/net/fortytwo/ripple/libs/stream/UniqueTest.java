package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class UniqueTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "(2 3) each >> unique >>", "2", "3" );
        assertReducesTo( "(2 3 2) each >> unique >>", "2", "3" );
        assertReducesTo( "(2 2) each >> unique >>", "2" );

        assertReducesTo( "((1 2) (2 1) (1 2)) each >> unique >>", "(1 2)", "(2 1)" );
    }

    public void testListEquivalence() throws Exception
    {
        assertReducesTo( "(() rdf:nil) each >> unique >>", "()" );

        reduce( "@prefix : <http://example.org/uniqueTest/> .\n"
                + "@define foo: 1 2 3 ." );
        assertReducesTo( ":foo", "(1 2 3)" );
        assertReducesTo( ":foo rdf:rest >>", "(2 3)" );
    }

    public void testArity() throws Exception
    {
        // 'unique' currently has an arity of exactly one, regardless of the
        // depth to which the stack should be reduced to determine equality
        // in a "transparent" fashion
        assertReducesTo( "(1 2 2) (1 2 dup >>) both >> apply >> unique >>", "1 2 2" );
        assertReducesTo( "(1 1 2) (1 dup >> 2) both >> apply >> unique >>", "1 1 2", "1 dup >> 2" );
    }

    public void testListTransparency() throws Exception
    {
        // Lists are currently not transparent to 'unique'
        assertReducesTo( "(2 2) (2 dup >>) both >> unique >>", "(2 2)", "(2 dup >>)" );
    }
}