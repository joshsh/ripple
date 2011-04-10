package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class DateTimeToMillisTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        // TODO: native syntax for xsd:long literals
        assertReducesTo( "\"2008-04-01T11:34:36+06:00\"^^xsd:dateTime dateTimeToMillis.", "\"1207028076000\"^^xsd:long" );
    }
}