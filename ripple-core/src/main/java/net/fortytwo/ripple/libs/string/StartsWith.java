/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.logic.LogicLibrary;

/**
 * A primitive which consumes a string and prefix, producing a Boolean value of
 * true if the given string starts with the given prefix, otherwise false.
 */
public class StartsWith extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public StartsWith()
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

		String affix, s;
		RippleValue result;

		affix = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = ( s.startsWith( affix ) )
			? LogicLibrary.getTrueValue()
			: LogicLibrary.getFalseValue();
		sink.put( arg.with(
			stack.push( result ) ) );
	}
}

