/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.etc.EtcLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes two strings and produces their
 * concatenation.
 */
public class StrCat extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "strCat",
            StringLibrary.NS_2007_08 + "strCat",
            EtcLibrary.NS_2007_05 + "strCat"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public StrCat()
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

		RippleValue strA, strB;
        String result;

		strA = stack.getFirst();
		stack = stack.getRest();
		strB = stack.getFirst();
		stack = stack.getRest();

		result = mc.toString( strB ) + mc.toString( strA );

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, strA, strB ) ) ) );
	}
}

