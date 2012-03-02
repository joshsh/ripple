/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string and produces its SHA-1 sum.
 */
public class Sha1 extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "sha1",
            StringLibrary.NS_2008_08 + "sha1",
            StringLibrary.NS_2007_08 + "sha1",
            SystemLibrary.NS_2007_05 + "sha1"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Sha1()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "plaintext", null, true )};
    }

    public String getComment()
    {
        return "finds the sha1 hash of a string";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		RippleValue a = stack.getFirst();
		stack = stack.getRest();

        String result = StringUtils.sha1SumOf( mc.toString( a ) );
        solutions.put(
				stack.push( StringLibrary.value( result, mc, a ) ) );
	}
}

