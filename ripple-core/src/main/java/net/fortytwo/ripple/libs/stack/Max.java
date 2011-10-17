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
 * A primitive which consumes a list and produces the greatest item in the list.
 */
public class Max extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "max",
            StackLibrary.NS_2008_08 + "max",
            StackLibrary.NS_2007_08 + "max"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Max()
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
        return "l  =>  x   -- where x is the greatest member of l";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext> solutions )
            throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( RippleList list ) throws RippleException
			{
				RippleValue result = null;
				while ( !list.isNil() )
				{
					RippleValue v = list.getFirst();
		
					if ( null == result || mc.getComparator().compare( v, result ) > 0 )
					{
						result = v;
					}
		
					list = list.getRest();
				}
		
				if ( null != result )
				{
					solutions.put( arg.with(
							rest.push( result ) ) );
				}
			}
		};

		mc.toList( l, listSink );
	}
}

