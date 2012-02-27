/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string, a regular expression and a replacement
 * substring, then produces the string obtained by replacing all occurrences of the
 * regular expression in the original string with the replacement substring.
 */
public class ReplaceAll extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "replace-all",
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

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true ),
                new Parameter( "regex", null, true ),
                new Parameter( "replacement", null, true )};
    }

    public String getComment()
    {
        return "s regex replacement  =>  s2 -- in which each occurrence of the given regular expression in s has been substituted with the given replacement";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

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

        solutions.put(
                stack.push( StringLibrary.value( result, mc, replacement, regex, s ) ) );

	}
}

