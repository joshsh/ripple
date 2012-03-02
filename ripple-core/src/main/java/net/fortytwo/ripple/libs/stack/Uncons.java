/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a list and produces the first item in the list,
 * followed by the rest of the list.
 */
public class Uncons extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "uncons",
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

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l", "a list", true )};
    }

    public String getComment()
    {
        return "l  =>  f r  -- where f is the first member of l and r is the rest of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				solutions.put(
						rest.push( list.getFirst() ).push( list.getRest() ) );
			}
		};

		mc.toList( l, listSink );
	}
}

