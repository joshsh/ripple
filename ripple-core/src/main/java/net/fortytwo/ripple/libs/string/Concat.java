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
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes two strings and produces their
 * concatenation.
 */
public class Concat extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "concat",
            StringLibrary.NS_2008_08 + "concat",
            StringLibrary.NS_2007_08 + "strCat",
            SystemLibrary.NS_2007_05 + "strCat"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Concat()
		throws RippleException
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
        return "s1 s2  =>  s3  -- where s3 is the concatenation of s1 and s2";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		RippleValue strA, strB;
        String result;

		strA = stack.getFirst();
		stack = stack.getRest();
		strB = stack.getFirst();
		stack = stack.getRest();

		result = mc.toString( strB ) + mc.toString( strA );

		solutions.put(
				stack.push( StringLibrary.value( result, mc, strA, strB ) ) );
	}
}

