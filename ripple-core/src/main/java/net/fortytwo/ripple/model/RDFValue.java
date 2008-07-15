/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/model/RDFValue.java $
 * $Revision: 63 $
 * $Author: parcour $
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
import org.openrdf.model.BNode;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 *  Note: this class has a natural ordering that is inconsistent with equals.
 */
public class RDFValue implements RippleValue
{
	private final Value value;
	
	public RDFValue( final Value value )
	{
		this.value = value;
	}

	public Value sesameValue()
	{
		return value;
	}

	public RDFValue toRDF( final ModelConnection mc )
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

    public boolean equals( final Object other )
    {
        // Note: Sesame's Value classes don't appear to have reasonable
        // implementations of equals() and hashCode().
        return ( other instanceof RDFValue )
                ? ( (RDFValue) other ).value.toString().equals( value.toString() )
                : false;
    }

    public int hashCode()
    {
        return 957832376 + value.toString().hashCode();
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
