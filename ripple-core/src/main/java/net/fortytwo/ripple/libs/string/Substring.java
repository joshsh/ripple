/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and two integer indexes, then
 * produces the substring between the first index (inclusive) and the second
 * index (exclusive).
 */
public class Substring extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

	public Substring()
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

		int begin, end;
		String s, result;

		end = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		begin = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		try
		{
			result = s.substring( begin, end );
			sink.put( arg.with(
					stack.push( mc.value( result ) ) ) );
		}

		catch ( IndexOutOfBoundsException e )
		{
			// Silent fail.
			return;
		}
	}
}

