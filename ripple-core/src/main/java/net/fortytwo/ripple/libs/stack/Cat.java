/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;

/**
 * A primitive function which consumes two lists and produces the concatenation
 * of the two lists.
 */
public class Cat extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "cat",
            StackLibrary.NS_2007_08 + "cat",
            StackLibrary.NS_2007_05 + "cat"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Cat()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l1", "a list", true ),
                new Parameter( "l2", "a list", true )};
    }

    public String getComment()
    {
        return "l1 l2  =>  l3  -- where l3 is the concatenation of Lists l1 and l2";
    }

	public void apply( final StackContext arg,
					   final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l1, l2;

		l1 = stack.getFirst();
		stack = stack.getRest();
		l2 = stack.getFirst();
		final RippleList rest = stack.getRest();
//System.out.println("l1 = " + l1 + ", l2 = " + l2);

		final Collector<RippleList, RippleException> firstLists = new Collector<RippleList, RippleException>();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list2 ) throws RippleException
			{
				Sink<RippleList, RippleException> catSink = new Sink<RippleList, RippleException>()
				{
					public void put( final RippleList list1 ) throws RippleException
					{
						RippleList result = list2.concat( list1 );
						solutions.put( arg.with(
								rest.push( result ) ) );
					}
				};

				firstLists.writeTo( catSink );
			}
		};

		mc.toList( l1, firstLists );
		mc.toList( l2, listSink );
	}
}

