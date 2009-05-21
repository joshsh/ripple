/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.libs.etc.EtcLibrary;

/**
 * A primitive which consumes a string and produces its (RFC 3986)
 * percent-encoded equivalent.
 */
public class PercentEncode extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "percentEncode",
            StringLibrary.NS_2007_08 + "percentEncode",
            EtcLibrary.NS_2007_05 + "urlEncoding"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public PercentEncode()
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
        return "finds the percent encoding (per RFC 3986) of a string";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue a = stack.getFirst();
		stack = stack.getRest();

		String result = StringUtils.percentEncode( mc.toString( a ) );
		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, a ) ) ) );
	}
}

