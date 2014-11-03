package net.fortytwo.ripple.cli;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.HistorySink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.jline.DirectiveCompletor;
import net.fortytwo.ripple.control.Scheduler;
import net.fortytwo.ripple.control.TaskQueue;
import net.fortytwo.ripple.control.ThreadedInputStream;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.ListGenerator;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.PipedIOStream;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.commands.DefineKeywordCmd;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A command-line read-eval-print loop which coordinates user interaction with a Ripple query engine.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleCommandLine {
    private static final Logger logger
            = Logger.getLogger(RippleCommandLine.class.getName());

    private static final byte[] EOL = {'\n'};

    private final PipedIOStream writeIn;
    //private PipedInputStream  writeIn;
    //private PipedOutputStream readOut;
    private final ThreadedInputStream consoleReaderInput;
    private final Interpreter interpreter;
    private final ConsoleReader reader;
    private final QueryEngine queryEngine;
    private final HistorySink<RippleList> queryResultHistory
            = new HistorySink<RippleList>(2);
    private final TaskQueue taskQueue = new TaskQueue();

    private int lineNumber;

    /**
     * Console input:
     * is to filter to consoleReaderInput to reader to readOut to writeIn to interpreter
     * <p/>
     * Normal output:
     * [commands and queries] to queryEngine.getPrintStream()
     * <p/>
     * Error output:
     * alert() to queryEngine.getErrorPrintStream()
     */
    public RippleCommandLine(final QueryEngine qe, final InputStream is)
            throws RippleException {
        queryEngine = qe;

        RecognizerAdapter ra = new RecognizerAdapter(qe.getErrorPrintStream()) {
            protected void handleQuery(final ListAST query) throws RippleException {
                addCommand(new VisibleQueryCommand(query, queryResultHistory));
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }

            protected void handleCommand(Command command) throws RippleException {
                addCommand(command);
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }

            protected void handleEvent(RecognizerEvent event) throws RippleException {
                switch (event) {
                    case NEWLINE:
                        readLine();
                        break;
                    case ESCAPE:
                        logger.fine("received escape event");
                        abortCommands();
                        break;
                    case QUIT:
                        logger.fine("received quit event");
                        abortCommands();
                        // Note: exception handling used for control
                        throw new ParserQuitException();
                    default:
                        throw new RippleException(
                                "event not yet supported: " + event);
                }
            }

            protected void handleAssignment(KeywordAST name) throws RippleException {
                Source<RippleList> source = queryResultHistory.get(0);
                if (null == source) {
                    source = new Collector<RippleList>();
                }

                addCommand(new DefineKeywordCmd(name, new ListGenerator(source)));
            }
        };

        Sink<Exception> parserExceptionSink = new ParserExceptionSink(
                qe.getErrorPrintStream());

        // Pass input through a filter to watch for special byte sequences, and
        // another draw input through it even when the interface is busy.
        InputStream filter = new InputStreamEventFilter(is, ra);
        consoleReaderInput = new ThreadedInputStream(filter);

        // Create reader.
        try {
            reader = new ConsoleReader(consoleReaderInput,
                    qe.getPrintStream());
        } catch (Throwable t) {
            throw new RippleException(t);
        }
        jline.Terminal term = reader.getTerminal();
//System.out.println( "reader.getTerminal() = " + term );

        writeIn = new PipedIOStream();
        //writeIn.write(32);
        //writeIn = new PipedInputStream();
        //readOut = new PipedOutputStream( writeIn );

        // Initialize completors.
        updateCompletors();

        // Create interpreter.
        interpreter = new Interpreter(ra, writeIn, parserExceptionSink);
    }

    public void run() throws RippleException {
        lineNumber = 0;
        interpreter.parse();
//System.out.println( "done parsing" );
    }

    private void readLine() {
        try {
            ++lineNumber;
            String prefix = "" + lineNumber + ")  ";
            //String prefix = "" + lineNumber + " >>  ";
            String expr = "";
            String line;
            do {
                line = reader.readLine(prefix);
                // TODO: handle end of file appropriately
                if (null == line) {
                    return;
                }

                expr += line + "\n";
            } while (line.trim().endsWith("\\"));

            // Feed the line to the lexer.
            byte[] bytes = expr.getBytes();
            //readOut.write( bytes, 0, bytes.length );
            writeIn.write(bytes, 0, bytes.length);

            // Add a newline character so the lexer will call readLine()
            // again when it gets there.
            //       writeIn.write(EOL);

            writeIn.flush();
        } catch (java.io.IOException e) {
            alert("IOException: " + e.toString());
        }
    }

    private void alert(final String s) {
        queryEngine.getErrorPrintStream().println("\n" + s + "\n");
    }

    private void updateCompletors() {
        logger.fine("updating completors");
        List<Completer> completors = new ArrayList<Completer>();

        try {
            Lexicon lex = queryEngine.getLexicon();

            synchronized (lex) {
                completors.add(lex.getCompletor());
            }

            ArrayList<String> directives = new ArrayList<String>();
            directives.add("@help");
            directives.add("@list");
            directives.add("@prefix");
            directives.add("@quit");
            directives.add("@relist");
            directives.add("@show");
            directives.add("@unlist");
            directives.add("@unprefix");

            completors.add(
                    new DirectiveCompletor(directives));

            try {
                // This makes candidates from multiple completors available at once.
                Completer multiCompletor = new AggregateCompleter(completors);

                reader.addCompleter(multiCompletor);
            } catch (Throwable t) {
                throw new RippleException(t);
            }
        } catch (RippleException e) {
            e.logError();
            logger.severe("failed to update completors");
        }
    }

    private class UpdateCompletorsCmd extends Command {
        public void execute(final QueryEngine qe, final ModelConnection mc)
                throws RippleException {
            updateCompletors();
        }

        public String getName() {
            // Note: never used
            return "update-completors";
        }

        protected void abort() {
        }
    }

    private void addCommand(final Command cmd) {
//System.out.println( "addCommand(" + cmd + ")" );
        cmd.setQueryEngine(queryEngine);
        taskQueue.add(cmd);
    }

    private void executeCommands() throws RippleException {
//System.out.println( "executeCommands()" );
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
//System.out.println( "abortCommands()" );
        taskQueue.stop();
    }
}

