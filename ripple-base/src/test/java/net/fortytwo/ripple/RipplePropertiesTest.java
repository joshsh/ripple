package net.fortytwo.ripple;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RipplePropertiesTest extends TestCase {
    public void testDates() throws Exception {
        RippleProperties props = new RippleProperties();

        props.setDate("prop1", new Date(0));
        //assertEquals( "1969-12-31T17:00:00-0700", props.getString( "prop1" ) );
        assertEquals(0, props.getDate("prop1").getTime());

        /* FIXME: restore me
        props.setString( "prop2", "2008-02-29T00:00:00-0800" );
        GregorianCalendar cal = new GregorianCalendar();
        cal.set( Calendar.YEAR, 2008 );
        cal.set( Calendar.MONTH, 1 );
        cal.set( Calendar.DATE, 29 );
        cal.set( Calendar.HOUR, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );
        cal.set( Calendar.ZONE_OFFSET, -8 * 60 * 60 * 1000 );
        assertEquals( cal.getTime(), props.getDate( "prop2" ) );

        props.setDate( "prop3", cal.getTime() );
        assertEquals( cal.getTime(), props.getDate( "prop3" ) );

        cal.set( Calendar.ZONE_OFFSET, 1 * 60 * 60 * 1000 );
        cal.set( Calendar.HOUR, 9 );
        assertEquals( cal.getTime(), props.getDate( "prop3" ) );
        //assertEquals( "2008-02-29T09:00:00+0100", props.getString( "prop3" ) );
        */
    }
}
