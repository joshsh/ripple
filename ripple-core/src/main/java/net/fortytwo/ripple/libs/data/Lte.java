/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes two items x and y and produces a Boolean value of
 * true if x is less than or equal to y according to Ripple total order, otherwise
 * false.
 */
public class Lte extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            DataLibrary.NS_2011_08 + "lte"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Lte()
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
        return "x y  =>  b  -- where b is true if x <= y, otherwise false";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		RippleValue a, b, result;

		b = stack.getFirst();
		stack = stack.getRest();
		a = stack.getFirst();
		stack = stack.getRest();

		result = mc.booleanValue(mc.getComparator().compare(a, b) <= 0);

		solutions.put(
				stack.push( result ) );
	}
}

