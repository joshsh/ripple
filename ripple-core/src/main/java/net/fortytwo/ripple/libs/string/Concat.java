/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.flow.Sink;

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

	public void apply( final StackContext arg,
						 final Sink<StackContext> solutions )
            throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue strA, strB;
        String result;

		strA = stack.getFirst();
		stack = stack.getRest();
		strB = stack.getFirst();
		stack = stack.getRest();

		result = mc.toString( strB ) + mc.toString( strA );

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, strA, strB ) ) ) );
	}
}

