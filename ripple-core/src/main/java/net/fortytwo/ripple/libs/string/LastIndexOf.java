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
 * A primitive which consumes a string and a substring and produces the index of
 * the last occurrence of the substring.
 */
public class LastIndexOf extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "last-index-of",
            StringLibrary.NS_2008_08 + "lastIndexOf",
            StringLibrary.NS_2007_08 + "lastIndexOf"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public LastIndexOf()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "str", null, true ),
                new Parameter( "substr", null, true )};
    }

    public String getComment()
    {
        return "str substr  =>  i -- where i is the index of the last occurrence of substr in str, or -1 if it does not occur";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext> solutions )
            throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String str, substr;
		int result;

		substr = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		str = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = str.lastIndexOf( substr );
		solutions.put( arg.with(
				stack.push( mc.numericValue(result) ) ) );
	}
}

