/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
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
 * A primitive which consumes a number and produces its absolute value.
 */
public class Abs extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_08 + "abs",
            MathLibrary.NS_2007_08 + "abs",
            MathLibrary.NS_2007_05 + "abs"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Abs()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = a.abs();

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

