/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/SesameInputAdapter.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.NamespaceImpl;
import org.openrdf.rio.RDFHandler;

/**
 * An RDFHandler which passes its input into an RdfSink.
 */
public class SesameInputAdapter implements RDFHandler
{
	private final Sink<Statement> stSink;
	private final Sink<Namespace> nsSink;
	private final Sink<String> cmtSink;

	public SesameInputAdapter( final RDFSink sink )
	{
		stSink = sink.statementSink();
		nsSink = sink.namespaceSink();
		cmtSink = sink.commentSink();
	}

	/**
	 *  Handles a comment.
	 */
	public void handleComment( final String comment )
	{
//System.out.println( "handleComment(\"" + comment + "\")" );
		try
		{
			cmtSink.put( comment );
		}

		catch ( Exception e )
		{
			// Log the error, but continue.
            logError( e );
		}
	}

	/**
	 *  Handles a namespace declaration/definition.
	 */
	public void handleNamespace( final String prefix, final String uri )
	{
//System.out.println( "handleNamespace(" + prefix + ", " + uri + ")" );
		try
		{
			nsSink.put( new NamespaceImpl( prefix, uri ) );
		}

		catch ( Exception e )
		{
			// Log the error, but continue.
            logError( e );
		}
	}

	/**
	 *  Handles a statement.
	 */
	public void handleStatement( final Statement st )
	{
//System.out.println( "handleStatement(" + st + ")" );
		try
		{
			stSink.put( st );
		}

		catch ( Exception e )
		{
			// Log the error, but continue.
			logError( e );
		}
	}

	/**
	 *  Signals the start of the RDF data.
	 */
	public void startRDF()
	{
	}

	/**
	 *  Signals the end of the RDF data.
	 */
	public void endRDF()
	{
	}

    private void logError( Exception e )
    {
        // FIXME
        System.err.println( "Error: " + e );
    }
}

