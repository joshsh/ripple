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
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a number and produces all real square roots of the
 * number.
 */
public class Sqrt extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_08 + "sqrt",
            MathLibrary.NS_2007_08 + "sqrt"};

    private final StackMapping self;

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Sqrt()
		throws RippleException
	{
		super();

        this.self = this;
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

		double a;

		a = mc.toNumericValue( stack.getFirst() ).doubleValue();
		stack = stack.getRest();

		// Apply the function only if it is defined for the given argument.
		if ( a >= 0 )
		{
			double d = Math.sqrt( a );

			// Yield both square roots.
			solutions.put( arg.with(
					stack.push( mc.value( d ) ) ) );
			if ( d > 0 )
			{
				solutions.put( arg.with(
						stack.push( mc.value( 0.0 - d ) ) ) );
			}
		}
	}

    @Override
    public StackMapping inverse()
    {
        return new Square();
    }

    private class Square implements StackMapping
    {
        public int arity()
        {
            return 1;
        }

        public StackMapping inverse() throws RippleException
        {
            return self;
        }

        public boolean isTransparent()
        {
            return true;
        }

        public void apply(StackContext arg, Sink<StackContext, RippleException> solutions) throws RippleException
        {
            final ModelConnection mc = arg.getModelConnection();
            RippleList stack = arg.getStack();

            NumericValue a, result;

            a = mc.toNumericValue( stack.getFirst() );
            stack = stack.getRest();

            result = a.mul( a );

            solutions.put( arg.with(
                    stack.push( result ) ) );
        }
    }
}

