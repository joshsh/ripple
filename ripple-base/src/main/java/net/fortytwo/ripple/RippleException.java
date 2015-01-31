package net.fortytwo.ripple;

/**
 * A single, custom Exception used for many purposes in Ripple
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleException extends Exception {
    private static final long serialVersionUID = 2498405641024203574L;

    public RippleException() {
        super();
    }

    public RippleException(final Throwable cause) {
        super(cause);
    }

    public RippleException(final String msg) {
        super(msg);
    }
}
