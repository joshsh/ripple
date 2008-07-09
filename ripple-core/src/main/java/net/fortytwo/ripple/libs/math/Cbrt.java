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
 * A primitive which consumes a number and produces its real cube root.
 */
public class Cbrt extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_06 + "cbrt",
            MathLibrary.NS_2007_08 + "cbrt"};

    private final StackMapping self;

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Cbrt()
		throws RippleException
	{
		super();

        this.self = this;
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

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.value( Math.cbrt( a.doubleValue() ) );

		solutions.put( arg.with(
				stack.push( result ) ) );
    }

    @Override
    public StackMapping inverse()
    {
        return new Cube();
    }

    private class Cube implements StackMapping
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

        public void applyTo(StackContext arg, Sink<StackContext, RippleException> solutions) throws RippleException
        {
            final ModelConnection mc = arg.getModelConnection();
            RippleList stack = arg.getStack();

            NumericValue a, result;

            a = mc.toNumericValue( stack.getFirst() );
            stack = stack.getRest();

            result = a.mul( a ).mul( a );

            solutions.put( arg.with(
                    stack.push( result ) ) );
        }
    }
}

