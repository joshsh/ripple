/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and produces its length.
 */
public class Length extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "length",
            StringLibrary.NS_2008_08 + "length",
            StringLibrary.NS_2007_08 + "length"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Length()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true )};
    }

    public String getComment()
    {
        return "s  =>  l -- where l is the length of string s";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		solutions.put(
				stack.push( mc.numericValue(s.length()) ) );
	}
}

