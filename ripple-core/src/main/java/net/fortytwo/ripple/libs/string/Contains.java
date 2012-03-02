/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which tells whether a string contains another string as a
 * substring.
 */
public class Contains extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "contains",
            StringLibrary.NS_2008_08 + "contains"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Contains() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s1", null, true ),
                new Parameter( "s2", null, true )};
    }

    public String getComment()
    {
        return "s1 s2  =>  b  -- where b is true/false if s1 contains s2 as a substring";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		String b = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		String a = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		RDFValue result = mc.booleanValue(a.contains(b));

		solutions.put(
				stack.push( result ) );
	}
}
