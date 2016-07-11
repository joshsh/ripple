package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class GetTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        getTestURIMap().put("http://example.org/getTest.txt", getClass().getResource("getTest.txt").toString());

        // FIXME: 'file' protocol is not supported by extras:get
        //assertReducesTo( "<http://example.org/getTest.txt> get.", "\"testing, one two three...\"" );
    }
}