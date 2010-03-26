/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string, strips off all leading and trailing
 * white space, and produces the result.
 */
public class Trim extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "trim",
            StringLibrary.NS_2007_08 + "trim"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Trim()
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
        return "s  =>  s2 -- where s2 is equal to s with leading and trailing white space omitted";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = mc.toString( s ).trim();

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, s ) ) ) );
	}
}

