/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes a string and produces its length.
 */
public class Length extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_04 + "length",
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

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( mc.value( s.length() ) ) ) );
	}
}

