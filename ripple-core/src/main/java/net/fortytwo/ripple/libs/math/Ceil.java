/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes a number and produces the smallest integer value
 * which is greater than or equal to the number.  Note: the produced value has
 * a type of xsd:integer, unlike Java's Math.ceil, which returns a double
 * value.
 */
public class Ceil extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "ceil",
            MathLibrary.NS_2008_08 + "ceil"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Ceil()
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
        return "x  =>  c, where c is the smallest integer value that is greater than or equal to x";
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

		result = mc.numericValue((int) Math.ceil(a.doubleValue()));

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

