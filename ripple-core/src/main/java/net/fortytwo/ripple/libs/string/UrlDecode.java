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

/**
 * A primitive which consumes an application/x-www-form-urlencoded string and
 * produces its decoded equivalent.
 */
public class UrlDecode extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "urlDecode"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public UrlDecode()
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

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = StringUtils.urlDecode( mc.toString( s ) );

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, s ) ) ) );
	}
}

