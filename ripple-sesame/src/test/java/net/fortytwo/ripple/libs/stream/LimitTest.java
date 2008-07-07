package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class LimitTest extends NewRippleTestCase
{
    public void testLimitLessThanSizeOfInput() throws Exception
    {
        assertReducesTo( "42 0 limit >>" );
        assertReducesTo( "42 (dup >> both >>) {1}>> 1 limit >>", "42" );
        assertReducesTo( "42 (dup >> both >>) {2}>> 3 limit >>", "42", "42", "42" );

        // risky... order of evaluation is undefined
        assertReducesTo( "(1 2 3) each >> 2 limit >>", "1", "2" );
    }
    
    public void testLimitEqualsSizeOfInput() throws Exception
    {
        assertReducesTo( "() each >> 0 limit >>" );
        assertReducesTo( "42 1 limit >>", "42" );
        assertReducesTo( "2 3 both >> 2 limit >>", "2", "3" );
    }

    public void testLimitGreaterThanSizeOfInput() throws Exception
    {
        assertReducesTo( "() each >> 1 limit >>" );
        assertReducesTo( "42 2 limit >>", "42" );
        assertReducesTo( "42 (dup >> both >>) {2}>> 1000 limit >>", "42", "42", "42", "42" );
    }
}