package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.test.RippleTestCase;

import java.util.Comparator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleValueComparatorTest extends RippleTestCase {
    public void testTypeComparison() throws Exception {
        ModelConnection mc = getTestModel().createConnection();
        Comparator<Object> c = mc.getComparator();

        assertTrue(c.compare(42, 42) == 0);
        assertTrue(c.compare(42.0, 42) == 0);
        assertTrue(c.compare(42, "foo") < 0);
        assertTrue(c.compare(42, new StackMappingWrapper(new NullStackMapping(), mc)) < 0);

        mc.close();
    }

    public void testLists() throws Exception {
        ModelConnection mc = getTestModel().createConnection();
        Comparator<Object> comparator = mc.getComparator();

        Number
                minusone = -1.0,
                one = 1,
                two = 2;

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