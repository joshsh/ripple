/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string, maps its characters to upper case, and
 * produces the result.
 */
public class ToUpperCase extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "toUpperCase",
            StringLibrary.NS_2007_08 + "toUpperCase"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToUpperCase()
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
        return "s  =>  s2 -- where s2 is equal to s with all characters converted to upper case";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = mc.toString( s ).toUpperCase();

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, s ) ) ) );
	}
}

