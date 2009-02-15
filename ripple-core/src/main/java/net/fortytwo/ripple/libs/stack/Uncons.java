/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes a list and produces the first item in the list,
 * followed by the rest of the list.
 */
public class Uncons extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "uncons",
            StackLibrary.NS_2007_08 + "uncons",
            StackLibrary.NS_2007_05 + "uncons"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Uncons()
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

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				solutions.put( arg.with(
						rest.push( list.getFirst() ).push( list.getRest() ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

