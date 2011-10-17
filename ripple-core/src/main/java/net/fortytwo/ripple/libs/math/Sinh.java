/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes a number and produces its hyperbolic sine.
 */
public class Sinh extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "sinh",
            MathLibrary.NS_2008_08 + "sinh",
            MathLibrary.NS_2007_08 + "sinh"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Sinh()
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
        return "x  =>  sinh(x)";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext> solutions )
            throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.numericValue(Math.sinh(a.doubleValue()));

		solutions.put( arg.with(
				stack.push( result ) ) );	}
}

