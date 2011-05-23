/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.math.MathLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes two items x and y and produces a Boolean value of
 * true if x is greater than y according to the natural ordering of x, otherwise
 * false.
 */
public class Gt extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            DataLibrary.NS_2011_04 + "gt",
            MathLibrary.NS_2008_08 + "gt",
            MathLibrary.NS_2007_08 + "gt",
            MathLibrary.NS_2007_05 + "gt"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Gt()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true )};
    }

    public String getComment()
    {
        return "x y  =>  b  -- where b is true if x > y, otherwise false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue a, b, result;

		b = stack.getFirst();
		stack = stack.getRest();
		a = stack.getFirst();
		stack = stack.getRest();

		result = mc.booleanValue(mc.getComparator().compare(a, b) > 0);

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

