package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.NumericValue;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class RandomTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        Collection<RippleList> results;
        RippleList l;
        RippleValue v;
        double d;
        Set<Double> values = new HashSet<Double>();
        
        for ( int i = 0; i < 1000; i++ )
        {
            results = reduce("random.");
            assertEquals( 1, results.size() );
            l = results.iterator().next();
            assertEquals( 1, l.length() );
            v = l.getFirst();
            assertTrue( v instanceof NumericValue );
            d = ( (NumericValue) v ).doubleValue();
            assertTrue( 0 <= d );
            assertTrue( 1 > d );
            Double dobj = new Double( d );
            assertFalse( values.contains( dobj ) );
            values.add( dobj );
        }
    }
}
