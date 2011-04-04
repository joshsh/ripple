/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.linkeddata.rdfizers;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.linkeddata.Rdfizer;
import net.fortytwo.linkeddata.ContextMemo;
import org.openrdf.model.URI;
import org.openrdf.rio.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: josh
 * Date: Jan 16, 2008
 * Time: 12:15:21 PM
 */
public class VerbatimRdfizer implements Rdfizer
{
	private final RDFParser parser;

	public VerbatimRdfizer( final RDFFormat format )
	{
		parser = Rio.createParser( format );
	}

	public ContextMemo.Status handle( final InputStream is,
						  final RDFHandler handler,
						  final URI documentUri,
						  final String baseUri )
	{
		try
		{
			parser.setRDFHandler( handler );
			parser.parse( is, baseUri );
		}

		catch ( IOException e )
		{
			new RippleException( e ).logError( false );
			return ContextMemo.Status.Failure;
		}

		catch ( RDFParseException e )
		{
			new RippleException( e ).logError( false );
			return ContextMemo.Status.ParseError;
		}

		catch ( RDFHandlerException e ) {
			new RippleException( e ).logError( false );
			return ContextMemo.Status.Failure;
		}

		return ContextMemo.Status.Success;
	}
}
