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
 * A primitive which consumes a list and produces the rest of the list, followed
 * by the first item in the list.
 */
public class Unswons extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "unswons",
            StackLibrary.NS_2007_08 + "unswons",
            StackLibrary.NS_2007_05 + "unswons"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Unswons()
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
        return "l  =>  r f  -- where f is the first member of l and r is the rest of l";
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
						rest.push( list.getRest() ).push( list.getFirst() ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

