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
 * A primitive which consumes an item and a list, prepends the item to the list,
 * then produces the resulting list.
 */
public class Cons extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "cons",
            StackLibrary.NS_2008_08 + "cons",
            StackLibrary.NS_2007_08 + "cons",
            StackLibrary.NS_2007_05 + "cons"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Cons()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "l", "a list", true )};
    }

    public String getComment()
    {
        return "x l  =>  l2  -- where the first member of l2 is x and the rest of l2 is l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;
		RippleValue l;

		l = stack.getFirst();
		stack = stack.getRest();
		final RippleValue x = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				solutions.put(
						rest.push( list.push( x ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

