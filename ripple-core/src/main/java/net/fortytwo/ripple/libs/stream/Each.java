/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes a list and produces each item in the list in a
 * separate stack.
 */
public class Each extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2008_08 + "each",
            StreamLibrary.NS_2007_08 + "each",
            StreamLibrary.NS_2007_05 + "each"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Each()
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
        return "l => each item in l";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( RippleList list ) throws RippleException
			{
				while ( !list.isNil() )
				{
                    try {
                        solutions.put( arg.with(
                                rest.push( list.getFirst() ) ) );
                    } catch (RippleException e) {
                        // Soft fail
                        e.logError();
                    }

					list = list.getRest();
				}
			}
		};

		mc.toList( l, listSink );
	}
}

