package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class Log10Test extends NewRippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 log10 >>", "0" );
        assertReducesTo( "10 log10 >>", "1" );
        assertReducesTo( "0.1 log10 >>", "-1" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "-1 log10 >>" );
    }

    public void testSpecialValues() throws Exception
    {
        // TODO
//        assertReducesTo( "\"NaN\"^^xsd:double log10 >>", "\"NaN\"^^xsd:double" );
//        assertReducesTo( "\"INF\"^^xsd:double log10 >>", "\"INF\"^^xsd:double" );
//        assertReducesTo( "0 log10 >>", "\"-INF\"^^xsd:double" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "0 log10 <<", "1" );
        assertReducesTo( "1 log10 <<", "10" );
        
        // TODO: this is a little odd
        assertReducesTo( "-1 log10 <<", "0" );
        assertReducesTo( "-1.0 log10 <<", "0.1" );
    }
}