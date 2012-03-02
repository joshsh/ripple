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

/**
 * A primitive which consumes a number and produces its hyperbolic cosine.
 */
public class Cosh extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "cosh",
            MathLibrary.NS_2008_08 + "cosh",
            MathLibrary.NS_2007_08 + "cosh"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Cosh()
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
        return "x  =>  cosh(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.numericValue(Math.cosh(a.doubleValue()));

		solutions.put(
				stack.push( result ) );
	}
}

