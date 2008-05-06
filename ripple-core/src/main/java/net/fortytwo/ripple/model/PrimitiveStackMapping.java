/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Mapping;
import net.fortytwo.ripple.flow.NullMapping;
import net.fortytwo.ripple.io.RipplePrintStream;

public abstract class PrimitiveStackMapping implements StackMapping, RippleValue
{
	private RdfValue rdfEquivalent = null;
//	private FunctionTypeAnnotation typeAnnotation = null;
	private boolean transparent;

	public PrimitiveStackMapping( final boolean transparent )
	{
		this.transparent = transparent;
	}
	
	public PrimitiveStackMapping()
	{
		transparent = true;
	}

	public PrimitiveStackMapping( final RdfValue v, final ModelConnection mc )
		throws RippleException
	{
		setRdfEquivalent( v, mc );
	}

	public void setRdfEquivalent( final RdfValue v, final ModelConnection mc )
		throws RippleException
	{
		rdfEquivalent = v;

//		typeAnnotation = new FunctionTypeAnnotation( v, mc );
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
		p.print( rdfEquivalent );
	}

	public RdfValue toRDF( final ModelConnection mc )
		throws RippleException
	{
		return rdfEquivalent;
	}

	public String toString()
	{
        // TODO: this guards against a null rdfEquivalent value, but this should't be allowed to happen
        return ( null == rdfEquivalent )
                ? "unknown"
                : "" + rdfEquivalent;
	}
	
	public boolean equals( final Object o )
	{
        return ( o instanceof PrimitiveStackMapping)
			? ( o == this ) : false;
	}

	public boolean isActive()
	{
		return false;
	}

	public int compareTo( final RippleValue other )
	{
//System.out.println( "[" + this + "].compareTo(" + other + ")" );
		if ( other instanceof PrimitiveStackMapping)
		{
			if ( other == this )
			{
				return 0;
			}

			else if ( this.hashCode() < other.hashCode() )
			{
				return -1;
			}

			else
			{
				return 1;
			}
		}

		else
		{
			return this.getClass().getName().compareTo( other.getClass().getName() );
		}
	}
	
	public boolean isTransparent()
	{
		return transparent;
	}

    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }

}

