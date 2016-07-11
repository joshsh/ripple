package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Sha1Test extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("\"mailto:josh@fortytwo.net\" sha1.", "\"1f62decdebec6594187ed1fa02355d9db33184fa\"");
        assertReducesTo("\"foo\" sha1.", "\"0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33\"");
        assertReducesTo("\"0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33\" sha1.",
                "\"2865765152809a426f118f48c468c5f459425211\"");

        // Empty string
        assertReducesTo("\"\" sha1.", "\"da39a3ee5e6b4b0d3255bfef95601890afd80709\"");
    }
}
