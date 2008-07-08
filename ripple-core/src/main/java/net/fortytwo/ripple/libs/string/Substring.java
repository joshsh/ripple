/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
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
 * A primitive which consumes a string and two integer indexes, then
 * produces the substring between the first index (inclusive) and the second
 * index (exclusive).
 */
public class Substring extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "substring",
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

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
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

