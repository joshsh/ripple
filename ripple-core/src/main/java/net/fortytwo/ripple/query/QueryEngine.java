package net.fortytwo.ripple.query;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;

import java.io.PrintStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class QueryEngine {
    private final Model model;
    private final Lexicon lexicon;
    private final StackEvaluator evaluator;
    private final RipplePrintStream printStream;
    private final PrintStream errorPrintStream;

    // this single connection is used for all query operations derived from this engine,
    // including the asynchronous SesameModelConnection query tasks which execute in worker threads.
    // This allows one thread to begin a query operation and another to finish it at any time;
    // the connection will still be active as long as the query engine has not been shut down.
    private final ModelConnection connection;

    public QueryEngine(final Model model) throws RippleException {
        this(model, new LazyStackEvaluator(), System.out, System.err);
    }

    public QueryEngine(final Model model,
                       final PrintStream out,
                       final PrintStream err) throws RippleException {
        this(model, new LazyStackEvaluator(), out, err);
    }

    public QueryEngine(final Model model,
                       final StackEvaluator evaluator,
                       final PrintStream out,
                       final PrintStream err) throws RippleException {
        this.model = model;
        this.evaluator = evaluator;
        lexicon = new Lexicon(model);
        printStream = new RipplePrintStream(out, lexicon);
        errorPrintStream = err;

        connection = model.createConnection(new LexiconUpdater(lexicon));

        initializeLexicon();

        // TODO: the default value is a temporary fix for version conflicts due to property renaming
        String defaultNamespace = Ripple.getConfiguration().getString(
                Ripple.DEFAULT_NAMESPACE, "http://example.org/ns/");

        // Set the default namespace.
        //mc.setNamespace( "", defaultNamespace, false );
        // FIXME: these should not be hard-coded
        getLexicon().addCommonNamespaces(connection);
        getLexicon().setNamespace("", defaultNamespace, connection);
        connection.commit();
    }

    public ModelConnection getConnection() {
        return connection;
    }

    public StackEvaluator getEvaluator() {
        return evaluator;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public Model getModel() {
        return model;
    }

    public RipplePrintStream getPrintStream() {
        return printStream;
    }

    public PrintStream getErrorPrintStream() {
        return errorPrintStream;
    }

    public void executeCommand(final Command cmd) throws RippleException {
        cmd.execute(this, connection);
    }

    public void shutDown() throws RippleException {
        connection.close();
    }

    private void initializeLexicon() throws RippleException {
        LexiconUpdater updater = new LexiconUpdater(lexicon);

        connection.getNamespaces().writeTo(updater.adderSink().namespaceSink());
        connection.commit();
    }
}

