/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a list, an "initial value" and a filter, then
 * produces the result of folding the list with the given filter and initial
 * value.  For instance, <code>(1 2 3) 0 add /fold</code> yields 0 + 1 + 2 + 3
 * = 6.
 */
public class Fold extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

	public Fold()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l;

		final RippleValue f = stack.getFirst();
		stack = stack.getRest();
		final RippleValue v = stack.getFirst();
		stack = stack.getRest();
		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				RippleList lList = list.invert();
				RippleList result = rest.push( v );
		
				while ( !lList.isNil() )
				{
					result = result.push( lList.getFirst() )
						.push( f )
						.push( Operator.OP );
					lList = lList.getRest();
				}
		
				sink.put( arg.with( result ) );
			}
		};

		mc.toList( l, listSink );
	}
}

