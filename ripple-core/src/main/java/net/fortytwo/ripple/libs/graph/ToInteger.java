/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

import org.apache.log4j.Logger;

/**
 * A primitive which consumes a literal value and produces its xsd:integer
 * equivalent (if any).
 */
public class ToInteger extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	private static final Logger LOGGER
		= Logger.getLogger( ToInteger.class );

	public ToInteger()
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

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		int i;

		try
		{
			i = new Integer( s ).intValue();
		}

		catch ( NumberFormatException e )
		{
			LOGGER.debug( "bad integer value: " + s );
			return;
		}

		sink.put( arg.with(
				stack.push( mc.value( i ) ) ) );
	}
}

// kate: tab-width 4
