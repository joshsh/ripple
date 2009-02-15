/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.query.QueryEngine;

public class BlankNodeAST implements AST<RippleList>
{
	private final String id;

	public BlankNodeAST( final String id )
	{
		this.id = id;
	}

	public String toString()
	{
		return "_:" + id;
	}

	public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		RippleValue v = new RDFValue( mc.createBNode( id ) );

		if ( null == v )
		{
			throw new RippleException( "blank node '" + this + "' does not exist" );
		}

		else
		{
			sink.put( mc.list( v ) );
		}
	}
}

