package net.fortytwo.ripple.query;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NullOutputStream extends OutputStream {
    public void write(int i) throws IOException {
        // Do nothing.
    }
}
