/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.flow.Sink;


public class ListDequotation implements StackMapping
{
	private final RippleList list;

	public ListDequotation( final RippleList list )
	{
		this.list = list;
	}

	public int arity()
	{
		return 0;
	}

	public void applyTo( final StackContext arg,
						final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
		RippleList stack = arg.getStack();

		RippleList in = list;
		RippleList out = stack;

		while ( !in.isNil() )
		{
			out = out.push( in.getFirst() );
			in = in.getRest();
		}

		// Never emit an empty stack.
		if ( !out.isNil() )
		{
			sink.put( arg.with( out ) );
		}
	}

	public boolean isTransparent()
	{
		return true;
	}

    // TODO: list dequotation mapping has a calculable inverse mapping
    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }

    public String toString()
    {
        return "Dequote(" + list + ")";
    }
}
