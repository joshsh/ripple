/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * Consumes a list and an index n and produces the nth item in the list, where
 * the first item in the list has an index of 1.
 */
public class At extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "at",
            StackLibrary.NS_2007_08 + "at",
            StackLibrary.NS_2007_05 + "at"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public At()
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
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		final int index = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		RippleValue l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( RippleList list ) throws RippleException
			{
                if ( list.isNil() )
                {
                    throw new RippleException( "list index out of bounds: " + index );
                }

                if ( index < 1 )
				{
					throw new RippleException( "list index out of bounds (note: 'at' begins counting at 1): " + index );
				}
		
				for ( int j = 1; j < index; j++ )
				{
					list = list.getRest();
					if ( list.isNil() )
					{
						throw new RippleException( "list index out of bounds: " + index );
					}
				}
		
				solutions.put( arg.with(
						rest.push( list.getFirst() ) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

