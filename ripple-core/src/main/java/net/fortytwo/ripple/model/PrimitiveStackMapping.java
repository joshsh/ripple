/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Resource;

public abstract class PrimitiveStackMapping implements StackMapping, RippleValue
{
    private final boolean transparent;

	private RDFValue rdfEquivalent;
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

    public void setRdfEquivalent( final RDFValue v, final ModelConnection mc )
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

	public RDFValue toRDF( final ModelConnection mc )
		throws RippleException
	{
        if ( null == rdfEquivalent )
        {
            rdfEquivalent = new RDFValue( mc.createBNode() );
        }

        return rdfEquivalent;
	}

	public String toString()
	{
        // TODO: this guards against a null rdfEquivalent value, but this should't be allowed to happen
        return ( null == rdfEquivalent )
                ? "[anonymous PrimitiveStackMapping]"
                : "" + rdfEquivalent;
	}

	public boolean isActive()
	{
		return false;
	}

	public boolean isTransparent()
	{
		return transparent;
	}

    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }

    public boolean equals( final Object other )
    {
        return ( other instanceof PrimitiveStackMapping )
                ? ( null == rdfEquivalent )
                        ? ( null == ( (PrimitiveStackMapping) other ).rdfEquivalent )
                        : false
                : ( null == ( (PrimitiveStackMapping) other ).rdfEquivalent )
                        ? false
                        : rdfEquivalent.equals( ( (PrimitiveStackMapping) other ).rdfEquivalent );
    }

    public int hashCode()
    {
        int code = 298357625;

        if ( null != rdfEquivalent )
        {
            code += rdfEquivalent.hashCode();
        }

        return code;
    }
}

