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
import org.openrdf.model.URI;
import org.openrdf.model.Resource;

public abstract class PrimitiveStackMapping implements StackMapping, RippleValue
{
    private final boolean transparent;

	private RdfValue rdfEquivalent;
//	private FunctionTypeAnnotation typeAnnotation = null;

	public PrimitiveStackMapping( final boolean transparent )
    {
		this.transparent = transparent;
    }
	
	public PrimitiveStackMapping()
    {
		this( true );
	}

    public abstract String[] getIdentifiers();

    public void setRdfEquivalent( final RdfValue v, final ModelConnection mc )
		throws RippleException
	{
        if ( !( v.sesameValue() instanceof Resource) )
        {
            throw new IllegalArgumentException( "for comparison purposes, the identifier of a PrimitiveStackMapping must be a Resource" );
        }

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
                ? "[anonymous PrimitiveStackMapping]"
                : "" + rdfEquivalent;
	}
	
	public boolean equals( final Object o )
	{
        return ( o instanceof RippleValue )
                ? 0 == compareTo( (RippleValue) o )
                : false;
	}

	public boolean isActive()
	{
		return false;
	}

    // TODO: this logic is incomplete
    public int compareTo( final RippleValue other )
	{
//System.out.println( "[" + this + "].compareTo(" + other + ")" );
		if ( other instanceof PrimitiveStackMapping
                && null != rdfEquivalent
                && null != ( (PrimitiveStackMapping) other ).rdfEquivalent )
		{
            return rdfEquivalent.compareTo( ( (PrimitiveStackMapping) other ).rdfEquivalent );
		}

        else if ( other instanceof RdfValue && null != rdfEquivalent )
        {
            return rdfEquivalent.compareTo( other );
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

