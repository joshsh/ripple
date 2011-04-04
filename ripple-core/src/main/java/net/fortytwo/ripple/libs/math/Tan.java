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
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number representing an angle in radians and
 * produces its tangent.
 */
public class Tan extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_08 + "tan",
            MathLibrary.NS_2007_08 + "tan"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Tan()
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
        return "x  =>  tan(x)";
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

// TODO: check for undefined values
		double d = Math.tan( a );
		result = mc.value( d );

		solutions.put( arg.with(
				stack.push( result ) ) );
    }

    @Override
    public StackMapping inverse() throws RippleException
    {
        return MathLibrary.getAtanValue();
    }
}

