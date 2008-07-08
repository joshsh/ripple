/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a string and a regular expression, then produces
 * the list obtained by "splitting" the string around the regular expression.
 * For instance <code>... "one, two,three" ",[ ]*" /split</code> yields
 * <code>... ("one" "two" "three")</code>
 */
public class Split extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "split",
            StringLibrary.NS_2007_08 + "split"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Split()
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

		RippleValue s, regex;

		regex = stack.getFirst();
		stack = stack.getRest();
		s = stack.getFirst();
		stack = stack.getRest();

		try
		{
			String [] array = mc.toString( s ).split( mc.toString( regex ) );
			RippleList result = mc.list();
			for ( int i = array.length - 1; i >= 0; i-- )
			{
				result = result.push( StringLibrary.value( array[i], mc, s, regex ) );
			}

			solutions.put( arg.with(
					stack.push( result ) ) );
		}

		catch ( java.util.regex.PatternSyntaxException e )
		{
			// Hard fail (for now).
			throw new RippleException( e );
		}
	}
}

