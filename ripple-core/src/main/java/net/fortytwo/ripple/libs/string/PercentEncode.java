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
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.libs.etc.EtcLibrary;

/**
 * A primitive which consumes a string and produces its (RFC 3986)
 * percent-encoded equivalent.
 */
public class PercentEncode extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "percentEncode",
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

		String a, result;

		a = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = StringUtils.percentEncode( a );
		sink.put( arg.with(
				stack.push( mc.value( result ) ) ) );
	}
}

