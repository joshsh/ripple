/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
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

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

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
                if ( null != v.getMapping() )
                {
                    StackMapping inverse = v.getMapping().getInverse();
                    out = out.getRest().push( new Operator( inverse ) );
                    solutions.put( out );
                }
            }

            else
            {
                solutions.put( out );
            }
        }
	}

	public boolean isTransparent()
	{
		return true;
	}

    public StackMapping getInverse() throws RippleException
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
