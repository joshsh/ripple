/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;

/**
 * A numeric (xsd:integer or xsd:double) literal value.
 */
public abstract class NumericValue implements RippleValue
{
	/**
	 * Distinguishes between numeric literals of type xsd:integer and xsd:double.
	 */
	public enum NumericLiteralType { INTEGER, LONG, DOUBLE, FLOAT };

	protected NumericLiteralType type;
	protected Number number;

// TODO: move into implementation
	protected RdfValue rdfEquivalent = null;

	public abstract RdfValue toRDF( final ModelConnection mc ) throws RippleException;
	
	public NumericLiteralType getType()
	{
		return type;
	}
	
	protected Number getNumber()
	{
		return number;
	}

	public int intValue()
	{
		return number.intValue();
	}

	public long longValue()
	{
		return number.longValue();
	}
	
	public double doubleValue()
	{
		return number.doubleValue();
	}
	
	public float floatValue()
	{
		return number.floatValue();
	}

	public boolean isZero()
	{
		return ( 0.0 == number.doubleValue() );
	}

	public int sign()
	{
		double x = number.doubleValue();
		return ( x == 0.0 ? 0 : x > 0 ? 1 : -1 );
	}

	////////////////////////////////////////////////////////////////////////////

	public boolean isActive()
	{
		return false;
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
		switch ( type )
		{
			case INTEGER:
				p.print( number.intValue() );
				break;
			case LONG:
				p.print( number.longValue() );
				break;
			case DOUBLE:
				p.print( number.doubleValue() );
				break;
			case FLOAT:
				p.print( number.floatValue() );
				break;
		}
	}

// TODO: implement hashCode()
	public boolean equals( final Object other )
	{
		if ( other instanceof NumericValue )
		{
			return ( 0 == compareTo( (NumericValue) other ) );
		}
		
		else
		{
			return false;
		}
	}
	
	public int compareTo( final RippleValue other )
	{
        // NumericValues are comparable with other NumericValues and with
        // numeric Literals.
        return ( other instanceof NumericValue )
		        ? RdfValue.compare( this, (NumericValue) other )
                : ( other instanceof RdfValue )
                        ? - RdfValue.compare( (RdfValue) other, this )
                        : RdfValue.defaultCompare( this, other );
	}

	public String toString()
	{
		return number.toString();
	}
		
	public abstract NumericValue abs();
	public abstract NumericValue neg();
	public abstract NumericValue add( final NumericValue b );
	public abstract NumericValue sub( final NumericValue b );
	public abstract NumericValue mul( final NumericValue b );
	public abstract NumericValue div( final NumericValue b );
	public abstract NumericValue mod( final NumericValue b );
	public abstract NumericValue pow( final NumericValue pow );
}

