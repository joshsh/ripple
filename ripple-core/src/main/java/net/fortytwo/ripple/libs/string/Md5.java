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
import net.fortytwo.ripple.StringUtils;


/**
 * A primitive which consumes a string and produces its SHA-1 sum.
 */
public class Md5 extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	public Md5()
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

		String a;

		a = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		sink.put( arg.with(
				stack.push(
			mc.value( StringUtils.md5SumOf( a ) ) ) ) );
	}
}
