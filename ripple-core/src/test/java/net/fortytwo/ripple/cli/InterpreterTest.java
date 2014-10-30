package net.fortytwo.ripple.cli;

import junit.framework.TestCase;
import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.PipedIOStream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class InterpreterTest extends TestCase {
    private static final long WAIT_INTERVAL = 100l;
    private static final byte[] NEWLINE = {'\n'};

    private int lineNumber;

    private int getLineNumber() {
        return lineNumber;
    }

    private void setLineNumber(int n) {
        lineNumber = n;
    }

    private void parse(final InputStream is, final boolean expectSuccess)
            throws Exception {
        lineNumber = 0;

        final PipedIOStream pio = new PipedIOStream();

        Collector<Exception> exceptions = new Collector<Exception>();
        RecognizerAdapter ra = new RecognizerAdapter(System.err) {
            @Override
            protected void handleQuery(ListAST query) throws RippleException {
                // Do nothing.
            }

            @Override
            protected void handleCommand(Command command) throws RippleException {
                // Do nothing.
            }

            @Override
            protected void handleEvent(RecognizerEvent event) throws RippleException {
                // Do nothing.
            }

            @Override
            protected void handleAssignment(KeywordAST name) throws RippleException {
                // Do nothing.
            }
        };
        final Interpreter interpreter = new Interpreter(ra, pio, exceptions);

        Thread interpreterThread = new Thread(new Runnable() {
            public void run() {
                try {
                    interpreter.parse();
                } catch (Exception e) {
                    // Throw out the error, for now.
                    e.printStackTrace();
                }
            }
        }, "InterpreterTest thread");

        interpreterThread.start();

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));

        String line;

        do {
            exceptions.clear();

            lineNumber++;
            System.out.println("line #" + lineNumber);
            line = reader.readLine();

            if (null == line) {
                break;
            }

            pio.write((line.trim() + '\n').getBytes());

            do {
                //System.out.println( "waiting " + WAIT_INTERVAL + " milliseconds" );
                synchronized (this) {
                    // FIXME: the first wait depends on a race condition
                    try {
                        wait(WAIT_INTERVAL);
                    } catch (InterruptedException e) {
                        throw new RippleException(e);
                    }
                }
            } while (Thread.State.RUNNABLE == interpreterThread.getState());

            if (expectSuccess) {
                if (exceptions.size() > 0) {
                    fail("Success case failed on line "
                            + lineNumber + ": " + line
                            + ", with exception = " + exceptions.iterator().next());
                }
            } else {
                if (exceptions.size() < 1) {
                    fail("Failure case succeeded on line "
                            + lineNumber + ": " + line);
                }
            }
        } while (true);


        pio.close();
    }

    public void testNothing() throws Exception {
    }
}
