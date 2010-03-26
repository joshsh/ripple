/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;


public class ListDequotation implements StackMapping
{
    private final RippleList list;
    private boolean invert = false;
    
    public ListDequotation( final RippleList list )
	{
		this.list = list;
	}

	public int arity()
	{
		return 0;
	}

	public void apply( final StackContext arg,
						final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
		RippleList stack = arg.getStack();

		RippleList in = list;
		RippleList out = stack;

		while ( !in.isNil() )
		{
			out = out.push( in.getFirst() );
			in = in.getRest();
		}

		// Never emit an empty stack.
		if ( !out.isNil() )
		{
            if ( invert )
            {
                RippleValue v = out.getFirst();
                if ( v.isActive() )
                {
                    StackMapping inverted = ( (Operator) v ).getMapping().inverse();
                    out = out.getRest().push( new Operator( inverted ) );
                    sink.put( arg.with( out ) );
                }
            }

            else
            {
                sink.put( arg.with( out ) );
            }
        }
	}

	public boolean isTransparent()
	{
		return true;
	}

    public StackMapping inverse() throws RippleException
    {
        ListDequotation m = new ListDequotation( list );
        m.invert = true;
        return m;
    }

    public String toString()
    {
        return "Dequote(" + list + ")";
    }
}
