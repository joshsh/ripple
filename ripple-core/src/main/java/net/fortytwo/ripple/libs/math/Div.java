/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a numerator and divisor and produces their
 * quotient.  If the divisor is 0, no value is produced.
 */
public class Div extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_08 + "div",
            MathLibrary.NS_2007_08 + "div",
            MathLibrary.NS_2007_05 + "div"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Div()
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

		NumericValue a, b, result;

		b = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();
		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		// Note: division by zero simply does not yield a result.
		if ( !b.isZero() )
		{
			result = a.div( b );

			solutions.put( arg.with(
					stack.push( result ) ) );
		}
	}

    @Override
    public StackMapping inverse() throws RippleException
    {
        return MathLibrary.getMulValue();
    }
}

