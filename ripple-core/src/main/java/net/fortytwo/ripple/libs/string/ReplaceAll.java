/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a string, a regular expression and a replacement
 * substring, then produces the string obtained by replacing all occurrences of the
 * regular expression in the original string with the replacement substring.
 */
public class ReplaceAll extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "replaceAll",
            StringLibrary.NS_2007_08 + "replaceAll"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ReplaceAll()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue regex, replacement, s;
        String result;

		replacement = stack.getFirst();
		stack = stack.getRest();
		regex = stack.getFirst();
		stack = stack.getRest();
		s = stack.getFirst();
		stack = stack.getRest();

		try
		{
			result = mc.toString( s ).replaceAll( mc.toString( regex ), mc.toString( replacement ) );
		}

		catch ( java.util.regex.PatternSyntaxException e )
		{
			// Hard fail (for now).
			throw new RippleException( e );
		}

        solutions.put( arg.with(
                stack.push( StringLibrary.value( result, mc, replacement, regex, s ) ) ) );

	}
}

