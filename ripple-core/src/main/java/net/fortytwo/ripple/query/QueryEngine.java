/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;

import java.io.PrintStream;

public class QueryEngine
{
	private final Model model;
	private final Lexicon lexicon;
	private final StackEvaluator evaluator;
	private final RipplePrintStream printStream;
	private final PrintStream errorPrintStream;

    ////////////////////////////////////////////////////////////////////////////

    public QueryEngine( final Model model ) throws RippleException {
        this(model, new LazyStackEvaluator(), System.out, System.err);
    }

    public QueryEngine( final Model model,
                    final PrintStream out,
                    final PrintStream err ) throws RippleException {
        this(model, new LazyStackEvaluator(), out, err);
    }

	public QueryEngine( final Model model,
						final StackEvaluator evaluator,
						final PrintStream out,
						final PrintStream err )
		throws RippleException
	{
		this.model = model;
		this.evaluator = evaluator;
		lexicon = new Lexicon( model );
		printStream = new RipplePrintStream( out, lexicon );
		errorPrintStream = err;

		initializeLexicon();

        String defaultNamespace = Ripple.getConfiguration().getString(
                Ripple.DEFAULT_NAMESPACE );

        // Set the default namespace.
		ModelConnection mc = getConnection();

        try {
            mc.setNamespace( "", defaultNamespace, false );
            mc.commit();
        } finally {
            mc.close();
        }

        // FIXME: these should not be hard-coded
        getLexicon().addCommonNamespaces();

		getLexicon().setNamespace("", defaultNamespace);
	}

	////////////////////////////////////////////////////////////////////////////

	public ModelConnection getConnection()
		throws RippleException
	{
		return model.createConnection( new LexiconUpdater( lexicon ) );
	}

	public StackEvaluator getEvaluator()
	{
		return evaluator;
	}

	public Lexicon getLexicon()
	{
		return lexicon;
	}

	public Model getModel()
	{
		return model;
	}

	public RipplePrintStream getPrintStream()
	{
		return printStream;
	}

	public PrintStream getErrorPrintStream()
	{
		return errorPrintStream;
	}

    ////////////////////////////////////////////////////////////////////////////

	public void executeCommand( final Command cmd ) throws RippleException
	{
		ModelConnection mc = getConnection();

        try {
            cmd.execute( this, mc );
		} finally {
            mc.close();
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private void initializeLexicon() throws RippleException
	{
		LexiconUpdater updater = new LexiconUpdater( lexicon );

		ModelConnection mc = getConnection();

        try {
            mc.getNamespaces().writeTo( updater.adderSink().namespaceSink() );
        } finally {
            mc.close();
        }
    }
}

