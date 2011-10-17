/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;

public class KeywordAST implements AST<RippleList>
{
	private final String name;

	public KeywordAST( final String name )
	{
		this.name = name;
	}

    public String getName()
    {
        return name;
    }
    
    public String toString()
	{
		return name;
	}

    public void evaluate( final Sink<RippleList> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		Sink<RippleValue> uriSink = new Sink<RippleValue>()
		{
			public void put(final RippleValue v) throws RippleException
			{
				sink.put( mc.list().push( v ) );
			}
		};

		qe.getLexicon().resolveKeyword(name, uriSink, mc, qe.getErrorPrintStream());
	}
}

