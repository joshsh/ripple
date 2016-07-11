package net.fortytwo.ripple.cli;

import jline.Completor;
import jline.ConsoleReader;
import jline.MultiCompletor;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.jline.DirectiveCompletor;
import net.fortytwo.ripple.control.Scheduler;
import net.fortytwo.ripple.control.TaskQueue;
import net.fortytwo.ripple.control.ThreadedInputStream;
import net.fortytwo.ripple.flow.CollectorHistory;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.PipedIOStream;
import net.fortytwo.ripple.query.QueryEngine;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A command-line interpreter/browser which coordinates user interaction with a Ripple query engine.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CommandLineInterface {
    private static final Logger logger
            = Logger.getLogger(CommandLineInterface.class);

    private static final byte[] EOL = {'\n'};

    private final PipedIOStream writeIn;
    private final ThreadedInputStream consoleReaderInput;
    private final Interpreter interpreter;
    private final ConsoleReader reader;
    private final QueryEngine queryEngine;
    private final CollectorHistory<RippleList, RippleException> queryResultHistory
            = new CollectorHistory<RippleList, RippleException>(2);
    private final TaskQueue taskQueue = new TaskQueue();

    private int lineNumber;
    private boolean lastQueryContinued = false;

    /**
     * Console input:
     * is --> filter --> consoleReaderInput --> reader --> readOut --> writeIn --> interpreter
     * <p/>
     * Normal output:
     * [commands and queries] --> queryEngine.getPrintStream()
     * <p/>
     * Error output:
     * alert() --> queryEngine.getErrorPrintStream()
     */
    public CommandLineInterface(final QueryEngine qe, final InputStream is)
            throws RippleException {
        queryEngine = qe;

        // Handling of queries
        Sink<ListAST, RippleException> querySink = new Sink<ListAST, RippleException>() {
            public void put(final ListAST ast) throws RippleException {
                addCommand(new VisibleQueryCommand(ast, queryResultHistory, lastQueryContinued));
                lastQueryContinued = false;
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };

        // Handling of "continuing" queries
        Sink<ListAST, RippleException> continuingQuerySink = new Sink<ListAST, RippleException>() {
            public void put(final ListAST ast) throws RippleException {
                addCommand(new VisibleQueryCommand(ast, queryResultHistory, lastQueryContinued));
                lastQueryContinued = true;
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };

        // Handling of commands
        Sink<Command, RippleException> commandSink = new Sink<Command, RippleException>() {
            public void put(final Command cmd) throws RippleException {
                addCommand(cmd);
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };

        // Handling of parser events
        Sink<RecognizerEvent, RippleException> eventSink = new Sink<RecognizerEvent, RippleException>() {
            public void put(final RecognizerEvent event)
                    throws RippleException {
                switch (event) {
                    case NEWLINE:
                        readLine();
                        break;
                    case ESCAPE:
                        logger.debug("received escape event");
                        abortCommands();
                        break;
                    case QUIT:
                        logger.debug("received quit event");
                        abortCommands();
                        // Note: exception handling used for control
                        throw new ParserQuitException();
                    default:
                        throw new RippleException(
                                "event not yet supported: " + event);
                }
            }
        };

        RecognizerAdapter ra = new RecognizerAdapter(
                querySink, continuingQuerySink, commandSink, eventSink, qe.getErrorPrintStream());

        Sink<Exception, RippleException> parserExceptionSink = new ParserExceptionSink(
                qe.getErrorPrintStream());

        // Pass input through a filter to watch for special byte sequences, and
        // another draw input through it even when the interface is busy.
        InputStream filter = new InputStreamEventFilter(is, ra);
        consoleReaderInput = new ThreadedInputStream(filter);

        // Create reader.
        try {
            reader = new ConsoleReader(consoleReaderInput,
                    new OutputStreamWriter(qe.getPrintStream()));
        } catch (Throwable t) {
            throw new RippleException(t);
        }
        jline.Terminal term = reader.getTerminal();

        writeIn = new PipedIOStream();

        // Initialize completors.
        updateCompletors();

        // Create interpreter.
        interpreter = new Interpreter(ra, writeIn, parserExceptionSink);
    }

    public void run() throws RippleException {
        lineNumber = 0;
        interpreter.parse();
    }

    private void readLine() {
        try {
            ++lineNumber;
            String prefix = "" + lineNumber + ")  ";
            String line = reader.readLine(prefix);

            if (null != line) {
                // Feed the line to the lexer.
                byte[] bytes = line.getBytes();
                //readOut.write( bytes, 0, bytes.length );
                writeIn.write(bytes, 0, bytes.length);

                // Add a newline character so the lexer will call readLine()
                // again when it gets there.
                writeIn.write(EOL);
            }
        } catch (java.io.IOException e) {
            alert("IOException: " + e.toString());
        }
    }

    private void alert(final String s) {
        queryEngine.getErrorPrintStream().println("\n" + s + "\n");
    }

    private void updateCompletors() {
        logger.debug("updating completors");
        List<Completor> completors = new ArrayList<Completor>();

        try {
            Lexicon lex = queryEngine.getLexicon();

            synchronized (lex) {
                completors.add(lex.getCompletor());
            }

            ArrayList<String> directives = new ArrayList<String>();
            directives.add("@count");
            directives.add("@define");
            directives.add("@help");
            directives.add("@list");
            directives.add("@prefix");
            directives.add("@quit");
            directives.add("@redefine");
            directives.add("@undefine");
            directives.add("@unprefix");

            completors.add(
                    new DirectiveCompletor(directives));

            try {
                // This makes candidates from multiple completors available at once.
                Completor multiCompletor = new MultiCompletor(completors);

                reader.addCompletor(multiCompletor);
            } catch (Throwable t) {
                throw new RippleException(t);
            }
        } catch (RippleException e) {
            logger.log(Level.SEVERE, "failed to update completors", e);
        }
    }

    private class UpdateCompletorsCmd extends Command {
        public void execute(final QueryEngine qe, final ModelConnection mc)
                throws RippleException {
            updateCompletors();
        }

        protected void abort() {
        }
    }

    private void addCommand(final Command cmd) {
        cmd.setQueryEngine(queryEngine);
        taskQueue.add(cmd);
    }

    private void executeCommands() throws RippleException {
        Scheduler.add(taskQueue);

        consoleReaderInput.setEager(true);

        try {
            taskQueue.waitUntilFinished();
        } catch (RippleException e) {
            consoleReaderInput.setEager(false);
            throw e;
        }

        consoleReaderInput.setEager(false);
    }

    private void abortCommands() {
        taskQueue.stop();
    }
}

