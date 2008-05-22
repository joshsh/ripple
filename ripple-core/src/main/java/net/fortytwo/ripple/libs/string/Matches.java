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
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.logic.LogicLibrary;

/**
 * A primitive which consumes a string and a regular expression, producing a
 * Boolean value of true if the regular expression matches the string, otherwise
 * false.
 */
public class Matches extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "matches",
            StringLibrary.NS_2007_08 + "matches"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Matches()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String regex, s;
		RippleValue result;

		regex = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		try
		{
			result = ( s.matches( regex ) )
				? LogicLibrary.getTrueValue()
				: LogicLibrary.getFalseValue();
			sink.put( arg.with(
					stack.push( result ) ) );
		}

		catch ( java.util.regex.PatternSyntaxException e )
		{
			// Hard fail (for now).
			throw new RippleException( e );
		}
	}
}

