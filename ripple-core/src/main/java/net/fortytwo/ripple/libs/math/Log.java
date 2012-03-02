/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces the natural logarithm of the
 * number.
 */
public class Log extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "log",
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

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		double a;
		NumericValue result;

		a = mc.toNumericValue( stack.getFirst() ).doubleValue();
		stack = stack.getRest();

		// Apply the function only if it is defined for the given argument.
		if ( a > 0 )
		{
			result = mc.numericValue(Math.log(a));

			solutions.put(
					stack.push( result ) );
		}
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return MathLibrary.getExpValue();
    }
}

