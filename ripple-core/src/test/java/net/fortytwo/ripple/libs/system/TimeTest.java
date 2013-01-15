package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TimeTest extends RippleTestCase {
    public void testAll() throws Exception {
        Collection<RippleList> r = reduce("time.");
        assertEquals(1, r.size());
        RippleList l = r.iterator().next();
        assertEquals(1, l.length());
        RippleValue v = l.getFirst();
        Value rv = v.toRDF(this.modelConnection).sesameValue();
        assertTrue(rv instanceof Literal);
        assertEquals(XMLSchema.LONG, ((Literal) rv).getDatatype());
        long t = ((Literal) rv).longValue();
        // April 10, 2011 will never come again.
        assertTrue(t > 1302390000000l);
        // If Ripple is still being compiled on April 10, 2111, it will be a great time for some spring cleaning :-)
        assertTrue(t < 4458063600000l);
    }
}