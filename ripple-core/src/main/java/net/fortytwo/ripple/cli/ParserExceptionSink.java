package net.fortytwo.ripple.cli;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

import java.io.PrintStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ParserExceptionSink implements Sink<Exception> {
    private final PrintStream errorPrintStream;

    public ParserExceptionSink(final PrintStream ps) {
        errorPrintStream = ps;
    }

    public void put(final Exception e) throws RippleException {
        // This happens, for instance, when the parser receives a value
        // which is too large for the target data type.  Non-fatal.
        if (e instanceof NumberFormatException) {
            alert(e.toString());
        }

        // Report lexer errors to user, but don't log them.
        else if (e instanceof TokenStreamException) {
            alert("Lexer error: " + e.toString());
        }

        // Report parser errors to user, but don't log them.
        else if (e instanceof RecognitionException) {
            alert("Parser error: " + e.toString());
        } else {
            alert("Strange error (see log for details): " + e.toString());
            (new RippleException(e)).logError(true);
        }
    }

    private void alert(final String s) {
        errorPrintStream.println("\n" + s + "\n");
    }
}
