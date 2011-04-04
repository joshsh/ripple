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
import net.fortytwo.ripple.StringUtils;

/**
 * A primitive which consumes a string and produces its
 * application/x-www-form-urlencoded equivalent.
 */
public class UrlEncode extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "urlEncode",
            StringLibrary.NS_2007_08 + "urlEncode"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
	public UrlEncode()
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
        return "finds the URL encoding (per application/x-www-form-urlencoded) of a string";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = StringUtils.urlEncode( mc.toString( s ) );

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, s ) ) ) );
	}
}

