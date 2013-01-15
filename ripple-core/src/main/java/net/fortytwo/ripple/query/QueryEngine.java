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

    ////////////////////////////////////////////////////////////////////////////

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

        initializeLexicon();

        // TODO: the default value is a temporary fix for version conflicts due to property renaming
        String defaultNamespace = Ripple.getConfiguration().getString(
                Ripple.DEFAULT_NAMESPACE, "http://example.org/ns/");

        // Set the default namespace.
        ModelConnection mc = createConnection();

        try {
            //mc.setNamespace( "", defaultNamespace, false );

            // FIXME: these should not be hard-coded
            getLexicon().addCommonNamespaces(mc);

            getLexicon().setNamespace("", defaultNamespace, mc);

            mc.commit();
        } finally {
            mc.close();
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    public ModelConnection createConnection() throws RippleException {
        return model.createConnection(new LexiconUpdater(lexicon));
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

    ////////////////////////////////////////////////////////////////////////////

    public void executeCommand(final Command cmd) throws RippleException {
        ModelConnection mc = createConnection();

        try {
            cmd.execute(this, mc);
            mc.commit();
        } finally {
            mc.close();
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private void initializeLexicon() throws RippleException {
        LexiconUpdater updater = new LexiconUpdater(lexicon);

        ModelConnection mc = createConnection();

        try {
            mc.getNamespaces().writeTo(updater.adderSink().namespaceSink());
            mc.commit();
        } finally {
            mc.close();
        }
    }
}

