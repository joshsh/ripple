package net.fortytwo.ripple.cli;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Interpreter {
    private static final Logger logger
            = Logger.getLogger(Interpreter.class.getName());

    private final RecognizerAdapter recognizerAdapter;
    private final InputStream input;
    private final Sink<Exception> exceptionSink;

    private boolean active = false;

    public Interpreter(final RecognizerAdapter adapter,
                       final InputStream input,
                       final Sink<Exception> exceptionSink) {
        recognizerAdapter = adapter;
        this.input = input;
        this.exceptionSink = exceptionSink;
    }

    public void quit() {
        active = false;
    }

    public void parse() throws RippleException {
        active = true;

//System.out.println( "-- parse" );
        // Break out when a @quit directive is encountered or a fatal error is
        // thrown.
        while (active) {
//System.out.println( "-- construct" );
            RippleLexer lexer = new RippleLexer(input);
            lexer.initialize(recognizerAdapter);
            RippleParser parser = new RippleParser(lexer);
            parser.initialize(recognizerAdapter);

            try {
//System.out.println( "-- antlr" );
                parser.nt_Document();

                // If the parser has exited normally, then we're done.
//System.out.println( "-- normal exit" );
                active = false;
            }

            // The parser has received a quit command.
            catch (ParserQuitException e) {
//System.out.println( "-- quit" );
                logger.fine("quit() called on Interpreter");

                active = false;
            }

            // TokenStreamIOException is considered fatal.  Two scenarios in
            // which it occurs are when the Interpreter thread has been
            // interrupted, and when the lexer has reached the end of input.

            // All other errors are assumed to be non-fatal.
            catch (Exception e) {
                // Handle non-fatal errors in an application-specific way.
                exceptionSink.put(e);
            }

            // If there's anything in the input buffer, it's because the parser
            // ran across a syntax error.  Clear the buffer, create a new lexer
            // and parser instance, and start afresh.
            // Note: this is a command-line usage scenario, and precludes
            // recovery from errors when the Interpreter is reading from a
            // pre-populated buffer.
            clear(input);
        }
    }

    private static void clear(final InputStream is) throws RippleException {
        try {
            int lim = is.available();
            for (int i = 0; i < lim; i++) {
                is.read();
            }
        } catch (IOException e) {
            throw new RippleException(e);
        }
    }
}

