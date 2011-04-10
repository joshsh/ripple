/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes a list and an item and produces a Boolean value of
 * true if the item is contained in the list, otherwise false.
 */
public class Has extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_04 + "has",
            StackLibrary.NS_2008_08 + "has",
            StackLibrary.NS_2007_08 + "has",
            StackLibrary.NS_2007_05 + "has"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Has()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l", "a list", true ),
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "l x  =>  b  -- where b is true if List l contains a member which is equal to x, otherwise false";
    }

	private boolean has( RippleList l, final RippleValue v, final ModelConnection mc )
		throws RippleException
	{
		while ( !l.isNil() )
		{
			if ( 0 == mc.getComparator().compare( l.getFirst(), v ) )
			{
				return true;
			}

			l = l.getRest();
		}

		return false;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l;

		final RippleValue x = stack.getFirst();
		stack = stack.getRest();
		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				solutions.put( arg.with(
						rest.push( mc.value( has( list, x, mc ) ) ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

