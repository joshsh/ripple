/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.util.Set;
import java.util.LinkedHashMap;

import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Value;

public class SpecialValueMap
{
    // This map is order-preserving such that the most recently-added keys
    // appear first in the keySet.  This is important for Lexicon, in which
    // a keyword maps to the most recently-added URI.
    private final LinkedHashMap<Value, RippleValue> rdfToNativeMap;

	public SpecialValueMap()
	{
		rdfToNativeMap = new LinkedHashMap<Value, RippleValue>();
	}

	/**
	 *  @return  a native data structure which is equated with the given RDF
	 *  value.  If there is no such data structure, the value itself.  This
	 *  method will never return <code>null</code>.
	 */
	public RippleValue get( final RdfValue rdf )
	{
		RippleValue rpl =  rdfToNativeMap.get( rdf.sesameValue() );

		if ( null == rpl )
		{
			rpl = rdf;
		}

		return rpl;
	}

	public RippleValue get( final Value v )
	{
		RippleValue rpl =  rdfToNativeMap.get( v );

		if ( null == rpl )
		{
			return new RdfValue( v );
		}

		else
		{
			return rpl;
		}
	}

    public void put( final Value key, final RippleValue value )
    {
        rdfToNativeMap.put( key, value );
    }

    public void put( final RippleValue key, final RippleValue value, final ModelConnection mc )
		throws RippleException
	{
		rdfToNativeMap.put( key.toRDF( mc ).sesameValue(), value );
	}

	public void add( final RippleValue v, final ModelConnection mc )
		throws RippleException
	{
		rdfToNativeMap.put( v.toRDF( mc ).sesameValue(), v );
	}
	
	public Set<Value> keySet()
	{
		return rdfToNativeMap.keySet();
	}
}
