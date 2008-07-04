/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which produces a random number between 0 and 1.
 */
public class Random extends PrimitiveStackMapping
{
	private static final int ARITY = 0;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_06 + "random",
            MathLibrary.NS_2007_08 + "random"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Random()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		NumericValue result;

		result = mc.value( Math.random() );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

