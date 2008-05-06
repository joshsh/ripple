/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;

public class TypedLiteralAST implements AST<RippleList>
{
	private String value;
	private AST type;

	public TypedLiteralAST( final String value, final AST type )
	{
		this.value = value;
		this.type = type;
	}

	public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		Sink<RippleList, RippleException> typeSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList l ) throws RippleException
			{
                RippleValue type = l.getFirst();
                sink.put( mc.list( mc.createTypedLiteral( value, type ) ) );
			}
		};
		
		type.evaluate( typeSink, qe, mc );
	}

	public String toString()
	{
		return "\"" + value + "\"^^" + type;
	}
}

