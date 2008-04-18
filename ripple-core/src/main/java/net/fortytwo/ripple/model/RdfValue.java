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
import net.fortytwo.ripple.StringUtils;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 *  Note: this class has a natural ordering that is inconsistent with equals.
 */
public class RdfValue implements RippleValue
{
	private Value value;
	
	public RdfValue( final Value value )
	{
		this.value = value;
	}

	public Value getRdfValue()
	{
		return value;
	}

	public RdfValue toRdf( final ModelConnection mc )
	{
		return this;
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
		p.print( value );
	}

	public boolean isActive()
	{
		return false;
	}

	public int compareTo( final RippleValue other )
	{
//System.out.println( "[" + this + "].compareTo(" + other + ")" );
		if ( other instanceof RdfValue )
		{
			if ( this.value instanceof Literal )
			{
				if ( ( (RdfValue) other ).value instanceof Literal )
				{
					return ( (Literal) this.value ).getLabel().compareTo(
						( (Literal) ( (RdfValue) other ).value ).getLabel() );
				}

				else
				{
					return value.getClass().getName().compareTo(
						( (RdfValue) other ).value.getClass().getName() );
				}
			}

			else if ( this.value instanceof URI )
			{
				if ( ( (RdfValue) other ).value instanceof URI )
				{
					return ( (URI) this.value ).toString().compareTo(
						( (URI) ( (RdfValue) other ).value ).toString() );
				}

				else
				{
					return value.getClass().getName().compareTo(
						( (RdfValue) other ).value.getClass().getName() );
				}
			}

			// hack...
			else
			{
				int hcThis = value.hashCode();
				int hcOther = ( (RdfValue) other ).value.hashCode();

				if ( hcThis == hcOther )
				{
					return 0;
				}

				else if ( hcThis < hcOther )
				{
					return -1;
				}

				else
				{
					return 1;
				}
			}
		}

		else
		{
			return RdfValue.class.getName().compareTo( other.getClass().getName() );
		}
	}

	public boolean equals( final Object other )
	{
		return ( other instanceof RdfValue )
			? value.equals( ( (RdfValue) other ).value )
			: false;
	}

	public int hashCode()
	{
		return value.hashCode();
	}

	public String toString()
	{
		if ( value instanceof URI )
		{
			URI uri = (URI) value;
			return "<" + uri.getNamespace() + uri.getLocalName() + ">";
		}

		else if ( value instanceof Literal )
		{
			Literal lit = (Literal) value;
			return "\"" + StringUtils.escapeString( lit.getLabel() ) + "\""
					+ ( (null != lit.getDatatype() ) ? "^^<" + lit.getDatatype() + ">" : "" );
		}

		else
		{
			// Use Sesame's toString() methods.
			return value.toString();
		}
	}
}

// kate: tab-width 4
