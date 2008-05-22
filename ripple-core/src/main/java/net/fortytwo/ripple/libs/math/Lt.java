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
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes two items x and y and produces a Boolean value of
 * true if x is less than y according to the natural ordering of x, otherwise
 * false.
 */
public class Lt extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2008_06 + "lt",
            MathLibrary.NS_2007_08 + "lt",
            MathLibrary.NS_2007_05 + "lt"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Lt()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		RippleValue a, b, result;
		RippleList stack = arg.getStack();

		b = stack.getFirst();
		stack = stack.getRest();
		a = stack.getFirst();
		stack = stack.getRest();

		result = ( a.compareTo( b ) < 0 )
			? LogicLibrary.getTrueValue()
			: LogicLibrary.getFalseValue();

		sink.put( arg.with(
				stack.push( result ) ) );
	}
}

