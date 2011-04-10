/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
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
 * A primitive which consumes a string and two integer indexes, then
 * produces the substring between the first index (inclusive) and the second
 * index (exclusive).
 */
public class Substring extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_04 + "substring",
            StringLibrary.NS_2008_08 + "substring",
            StringLibrary.NS_2007_08 + "substring"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Substring()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true ),
                new Parameter( "beginIndex", null, true ),
                new Parameter( "endIndex", null, true )};
    }

    public String getComment()
    {
        return "s beginIndex endIndex  =>  s2 -- where s2 is the substring of s which begins at the specified beginIndex and extends to the character at index endIndex - 1";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		int begin, end;
		RippleValue s;
        String result;

		end = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		begin = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		s = stack.getFirst();
		stack = stack.getRest();

		try
		{
			result = mc.toString( s ).substring( begin, end );
			solutions.put( arg.with(
					stack.push( StringLibrary.value( result, mc, s ) ) ) );
		}

		catch ( IndexOutOfBoundsException e )
		{
			// Silent fail.
			return;
		}
	}
}

