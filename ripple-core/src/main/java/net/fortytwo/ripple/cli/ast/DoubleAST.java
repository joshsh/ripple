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
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

public class DoubleAST implements AST<RippleList>
{
	private final double value;

	public DoubleAST( final double value )
	{
		this.value = value;
	}

    public DoubleAST( final String rep )
    {
        String s = rep.startsWith("+")
                ? rep.substring(1)
                : rep;

        value = ( new Double( s ) ).doubleValue();
    }
    
    public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		sink.put( mc.list( mc.value( value ) ) );
	}

	public String toString()
	{
		return "" + value;
	}
}

