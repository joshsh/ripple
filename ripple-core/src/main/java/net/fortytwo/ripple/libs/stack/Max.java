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
 * A primitive which consumes a list and produces the greatest item in the list.
 */
public class Max extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	public Max()
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
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( RippleList list ) throws RippleException
			{
				RippleValue result = null;
				while ( !list.isNil() )
				{
					RippleValue v = list.getFirst();
		
					if ( null == result || v.compareTo( result ) > 0 )
					{
						result = v;
					}
		
					list = list.getRest();
				}
		
				if ( null != result )
				{
					sink.put( arg.with(
							rest.push( result ) ) );
				}
			}
		};

		mc.toList( l, listSink );
	}
}

