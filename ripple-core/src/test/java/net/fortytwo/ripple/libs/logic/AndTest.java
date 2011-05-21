package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AndTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "true true and.", "true" );
        assertReducesTo( "true false and.", "false" );
        assertReducesTo( "false true and.", "false" );
        assertReducesTo( "false false and.", "false" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "true 42 and.", "false" );
        assertReducesTo( "false 42 and.", "false" );
        assertReducesTo( "42 true and.", "false" );
        assertReducesTo( "42 false and.", "false" );
        assertReducesTo( "42 42 and.", "false" );
    }

    public void testInverse() throws Exception {
        assertReducesTo( "true and~.", "true true");
        assertReducesTo( "false and~.", "true false", "false true", "false false");
    }
}