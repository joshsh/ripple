package net.fortytwo.ripple.cli;

/**
 * A trivial exception for breaking out of the ANTLR-generated parser, which
 * does not match end-of-input.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ParserQuitException extends RuntimeException {
    private static final long serialVersionUID = -9033960706498220927L;
}

