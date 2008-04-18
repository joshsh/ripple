/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes an item and a list and produces a Boolean value of
 * true if the item is contained in the list, otherwise false.
 */
public class In extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public In()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	private boolean has( RippleList l, final RippleValue v )
		throws RippleException
	{
		while ( !l.isNil() )
		{
			if ( 0 == l.getFirst().compareTo( v ) )
			{
				return true;
			}

			l = l.getRest();
		}

		return false;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l;

		l = stack.getFirst();
		stack = stack.getRest();
		final RippleValue x = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				sink.put( arg.with(
						rest.push( has( list, x ) ? StackLibrary.getTrueValue() : StackLibrary.getFalseValue() ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

// kate: tab-width 4
