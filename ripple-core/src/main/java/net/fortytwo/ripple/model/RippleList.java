/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.ListNode;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.enums.ExpressionOrder;

public abstract class RippleList extends ListNode<RippleValue> implements RippleValue
{
    // Constants
    private static ExpressionOrder expressionOrder;
    private static boolean printPadded;
    private static boolean initialized = false;

    protected RippleValue first;
	protected RippleList rest;

	public RippleValue getFirst()
	{
		return first;
	}

	public RippleList getRest()
	{
		return rest;
	}

	public int length()
	{
		int l = 0;

		RippleList cur = this;
		while ( !cur.isNil() )
		{
			l++;
			cur = cur.getRest();
		}

		return l;
	}
	
	public RippleValue get( final int i )
		throws RippleException
	{
		if ( i < 0 )
		{
			throw new RippleException( "list index out of bounds: " + i );
		}

		RippleList cur = this;
		for ( int j = 0; j < i; j++ )
		{
			if ( cur.isNil() )
			{
				throw new RippleException( "list index out of bounds: " + i );
			}

			cur = cur.getRest();
		}

		return cur.getFirst();
	}
	
	public abstract RippleList push( RippleValue v ) throws RippleException;
    public abstract RippleList invert();

    private static void initialize() throws RippleException
    {
        RippleProperties props = Ripple.getProperties();
        expressionOrder = ExpressionOrder.find(props.getString( Ripple.EXPRESSION_ORDER ) );
        printPadded = props.getBoolean( Ripple.LIST_PADDING );
        initialized = true;
    }

    public String toString()
	{
        if ( !initialized )
        {
            try {
                initialize();
            } catch (RippleException e) {
                initialized = true;
                e.logError();
            }
        }

        StringBuilder sb = new StringBuilder();

		RippleList cur =
			( ExpressionOrder.DIAGRAMMATIC == expressionOrder )
			? this: invert();

		sb.append( printPadded ? "( " : "(" );

		boolean isFirst = true;
		while ( !cur.isNil() )
		{
			RippleValue val = cur.getFirst();

			if ( isFirst )
			{
				isFirst = false;
			}

            else
            {
				sb.append( " " );
			}

			if ( Operator.OP == val )
			{
				sb.append( ">>" );
			}

			else
			{
				sb.append( val );
			}

			cur = cur.getRest();
		}

		sb.append( printPadded ? " )" : ")" );

		return sb.toString();
	}


	// Note: assumes diagrammatic order
	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
        if ( !initialized )
        {
            initialize();
        }

		RippleList cur =
			( ExpressionOrder.DIAGRAMMATIC == expressionOrder )
			? this : invert();

		p.print( printPadded ? "( " : "(" );

		boolean isFirst = true;
		while ( !cur.isNil() )
		{
			RippleValue val = cur.getFirst();

			if ( isFirst )
			{
				isFirst = false;
			}

			else
			{
				p.print( " " );
			}

			if ( Operator.OP == val )
			{
				p.print( ">>" );
			}

			else
			{
				p.print( val );
			}

			cur = cur.getRest();
		}

		p.print( printPadded ? " )" : ")" );
	}
}
