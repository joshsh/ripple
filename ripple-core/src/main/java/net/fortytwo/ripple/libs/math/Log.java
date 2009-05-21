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
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a number and produces the natural logarithm of the
 * number.
 */
public class Log extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_08 + "log",
            MathLibrary.NS_2007_08 + "log"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Log()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x  =>  natural logarithm of x";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		double a;
		NumericValue result;

		a = mc.toNumericValue( stack.getFirst() ).doubleValue();
		stack = stack.getRest();

		// Apply the function only if it is defined for the given argument.
		if ( a > 0 )
		{
			result = mc.value( Math.log( a ) );

			solutions.put( arg.with(
					stack.push( result ) ) );
		}
	}

    @Override
    public StackMapping inverse() throws RippleException
    {
        return MathLibrary.getExpValue();
    }
}

