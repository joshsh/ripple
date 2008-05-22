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
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes two numbers and produces their sum.
 */
public class Add extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_06 + "add",
            MathLibrary.NS_2007_08 + "add",
            MathLibrary.NS_2007_05 + "add"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Add()
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

		NumericValue a, b, result;

		b = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();
		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = a.add( b );

		sink.put( arg.with(
				stack.push( result ) ) );
	}

    @Override
    public StackMapping inverse() throws RippleException
    {
        return MathLibrary.getSubValue();
    }
}

