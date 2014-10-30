package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Interpreter {
    private static final Logger LOGGER
            = Logger.getLogger(Interpreter.class);

    private final RecognizerAdapter recognizerAdapter;
    private final InputStream input;
    private final Sink<Exception, RippleException> exceptionSink;

    private boolean active = false;

    public Interpreter(final RecognizerAdapter adapter,
                       final InputStream input,
                       final Sink<Exception, RippleException> exceptionSink) {
        recognizerAdapter = adapter;
        this.input = input;
        this.exceptionSink = exceptionSink;
    }

    public void quit() {
        active = false;
    }

    public void parse() throws RippleException {
        active = true;

        System.out.println("-- parse");
        // Break out when a @quit directive is encountered or a fatal error is
        // thrown.
        while (active) {
            System.out.println("-- construct");

            CharStream s;

            System.out.println("debug 1");
            try {
                s = new ANTLRInputStream(input, "UTF-8");
            } catch (IOException e) {
                throw new RippleException(e);
            }

            System.out.println("debug 2");
            RippleLexer lexer = new RippleLexer(s);
            System.out.println("debug a");
            CommonTokenStream tokens = new CommonTokenStream();
            System.out.println("debug s");
            tokens.setTokenSource(lexer);
            RippleParser parser = new RippleParser(tokens);
            System.out.println("debug d");
            lexer.initialize(recognizerAdapter);
            System.out.println("debug f");
            parser.initialize(recognizerAdapter);

            try {
                System.out.println("-- antlr");
                parser.document();

                // If the parser has exited normally, then we're done.
                System.out.println("-- normal exit");
                active = false;
            }

            // The parser has received a quit command.
            catch (ParserQuitException e) {
                System.out.println("-- quit");
                LOGGER.debug("quit() called on Interpreter");

                active = false;
            }

            /*   FIXME: what is the ANTLR 3 equivalent of this TokenStreamIOException condition?
            // TokenStreamIOException is considered fatal.  Two scenarios in
            // which it occurs are when the Interpreter thread has been
            // interrupted, and when the lexer has reached the end of input.
            catch (TokenStreamIOException e) {
                new RippleException(e).logError();
                active = false;
                break;
            }
            */

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

