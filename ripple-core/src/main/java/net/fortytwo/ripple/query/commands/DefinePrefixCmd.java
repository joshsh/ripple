/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.cli.ast.URIAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;

import org.openrdf.model.URI;
import org.openrdf.model.impl.NamespaceImpl;

public class DefinePrefixCmd extends Command
{
	private String prefix;
	private URIAST uri;

	public DefinePrefixCmd( final String prefix, final URIAST uri )
	{
		this.prefix = prefix;
		this.uri = uri;
	}

    public String getPrefix()
    {
        return prefix;
    }

    public URIAST getUri()
    {
        return uri;
    }

    public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		Collector<RippleList, RippleException> sink = new Collector<RippleList, RippleException>();
		uri.evaluate( sink, qe, mc );

		if ( sink.size() == 0 )
		{
			throw new RippleException( "URI could not be constructed from " + uri );
		}

		else if ( sink.size() > 1 )
		{
			throw new RippleException( "multiple values constructed from " + uri );
		}

		// TODO: check that the list has exactly one element
		URI ns = mc.toUri( sink.iterator().next().getFirst() );
		
		mc.setNamespace( prefix, ns.toString(), true );

		// Note: when a namespace is manually defined, it may both override an
		// existing prefix with the same name, or duplicate another namespace
		// with the same URI.
		qe.getLexicon().add( new NamespaceImpl( prefix, ns.toString() ) );
	}

	protected void abort()
	{
	}
}

