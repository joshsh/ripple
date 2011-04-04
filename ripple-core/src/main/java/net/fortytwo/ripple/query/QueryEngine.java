/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QueryEngine
{
	private static final Logger LOGGER = Logger.getLogger( QueryEngine.class );

	private final Model model;
	private final Lexicon lexicon;
	private final StackEvaluator evaluator;
	private final RipplePrintStream printStream;
	private final PrintStream errorPrintStream;

    ////////////////////////////////////////////////////////////////////////////

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

//System.out.println("--- q");
		initializeLexicon();
//System.out.println("--- w");

        String defaultNamespace = Ripple.getConfiguration().getString(
                Ripple.DEFAULT_NAMESPACE );

        // Set the default namespace.
		ModelConnection mc = getConnection( "Demo connection" );

        try {
//System.out.println("--- w2");
//System.out.println("--- Ripple.defaultNamespace() = " + Ripple.defaultNamespace());
            mc.setNamespace( "", defaultNamespace, false );
//System.out.println("--- w3");
            mc.commit();
//System.out.println("--- w4");
        } finally {
            mc.close();
        }
//System.out.println("--- e");
		
		getLexicon().addNamespace( new org.openrdf.model.impl.NamespaceImpl( "", defaultNamespace ) );
//System.out.println("--- r");
	}

	////////////////////////////////////////////////////////////////////////////

	public ModelConnection getConnection()
		throws RippleException
	{
		return getConnection( null );
	}

	public ModelConnection getConnection( final String name )
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

	public String getDefaultNamespace()
		throws RippleException
	{
		String defaultNs = lexicon.resolveNamespacePrefix( "" );

		if ( null == defaultNs )
		{
			throw new RippleException( "no default namespace is defined.  Use '@prefix : <...>.'\n" );
		}

		return defaultNs;
	}

	public void uriForKeyword( final String localName, final Sink<RippleValue, RippleException> sink, final ModelConnection mc )
		throws RippleException
	{
		Collection<URI> options = lexicon.uriForKeyword( localName );

        // Creating a set of values eliminates the possibility of a keyword
        // resolving to the same runtime value more than once (as is the case,
        // for instance, when two or more URIs mapping to a special value have
        // the same local name).
        Set<RippleValue> uniqueValues = new HashSet<RippleValue>();
        for ( URI u : options )
        {
            uniqueValues.add( mc.canonicalValue( u ) );
        }

		if ( 0 == uniqueValues.size() )
		{
			errorPrintStream.println( "Warning: keyword '" + localName + "' is not defined\n" );
		}

        else if ( 1 < uniqueValues.size() )
		{
			errorPrintStream.println( "Warning: keyword '" + localName + "' is ambiguous\n" );
		}

        for ( RippleValue v : uniqueValues )
        {
            sink.put( v );
        }
	}

	public void uriForQName( final String nsPrefix,
								final String localName,
								final Sink<RippleValue, RippleException> sink,
								final ModelConnection mc )
		throws RippleException
	{
		String ns = lexicon.resolveNamespacePrefix( nsPrefix );

		if ( null == ns )
		{
			errorPrintStream.println( "Warning: prefix '" + nsPrefix + "' does not identify a namespace\n" );
		}

		else
		{
			sink.put( mc.uriValue( ns + localName ) );
		}
	}

    ////////////////////////////////////////////////////////////////////////////

	public void executeCommand( final Command cmd ) throws RippleException
	{
		ModelConnection mc = getConnection( "for executeCommand" );

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
            // TODO: restore me
            /*
            if ( Ripple.getProperties().getBoolean( Ripple.PULL_ENTIRE_MODEL ) );
            {
                mc.getStatements( null, null, null, updater.adderSink().statementSink(), false );
            }*/

            mc.getNamespaces().writeTo( updater.adderSink().namespaceSink() );
        } finally {
            mc.close();
        }
    }
}

