package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

import java.util.Comparator;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class RippleValueComparatorTest extends RippleTestCase {
    public void testTypeComparison() throws Exception {
        ModelConnection mc = getTestModel().createConnection();
        Comparator<RippleValue> c = mc.getComparator();

        assertTrue(c.compare(mc.numericValue(42), mc.numericValue(42)) == 0);
        assertTrue(c.compare(mc.numericValue(42.0), mc.numericValue(42)) == 0);
        assertTrue(c.compare(mc.numericValue(42), mc.plainValue("foo")) < 0);
        assertTrue(c.compare(mc.numericValue(42), new StackMappingWrapper(new NullStackMapping(), mc)) < 0);

        mc.close();
    }

    public void testLists() throws Exception {
        ModelConnection mc = getTestModel().createConnection();
        Comparator<RippleValue> comparator = mc.getComparator();

        RippleValue
                minusone = mc.numericValue(-1.0),
                one = mc.numericValue(1),
                two = mc.numericValue(2);

        assertEquals(0, comparator.compare(
                createStack(mc, one, two),
                createStack(mc, one, two)));
        assertEquals(-1, comparator.compare(
                createStack(mc, one),
                createStack(mc, one, two)));

        // ...

        mc.close();
    }

    // ...
}