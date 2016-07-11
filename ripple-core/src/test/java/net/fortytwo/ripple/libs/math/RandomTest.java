package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RandomTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        Collection<RippleList> results;
        RippleList l;
        Object v;
        double d;
        Set<Double> values = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            results = reduce("random.");
            assertEquals(1, results.size());
            l = results.iterator().next();
            assertEquals(1, l.length());
            v = l.getFirst();
            assertTrue(v instanceof Number);
            d = ((Number) v).doubleValue();
            assertTrue(0 <= d);
            assertTrue(1 > d);
            Double dobj = new Double(d);
            assertFalse(values.contains(dobj));
            values.add(dobj);
        }
    }
}
