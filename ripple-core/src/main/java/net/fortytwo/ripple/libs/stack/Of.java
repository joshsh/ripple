/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
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
 * Consumes an index n and a list and produces the nth item in the list, where
 * the first item in the list has an index of 1.
 */
public class Of extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "of",
            StackLibrary.NS_2007_08 + "of",
            StackLibrary.NS_2007_05 + "of"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Of()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "i", "a list index", true ),
                new Parameter( "l", "a list", true )};
    }

    public String getComment()
    {
        return "i l  =>  l[i]  -- pushes the member of List l at index i.  Note: lists are 1-indexed";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		RippleValue l;

		l = stack.getFirst();
		stack = stack.getRest();
		final int i = mc.toNumericValue( stack.getFirst() ).intValue();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( RippleList list ) throws RippleException
			{
				if ( i < 1 )
				{
					throw new RippleException( "list index out of bounds (keep in mind that 'at' begins counting at 1): " + i );
				}
		
				for ( int j = 1; j < i; j++ )
				{
					list = list.getRest();
					if ( list.isNil() )
					{
						throw new RippleException( "list index out of bounds: " + i );
					}
				}
		
				solutions.put( arg.with(
						rest.push( list.getFirst() ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

