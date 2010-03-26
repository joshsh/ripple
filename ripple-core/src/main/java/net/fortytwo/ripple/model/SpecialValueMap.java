/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpecialValueMap
{
    private final Map<Value, RippleValue> rdfToNativeMap;

	public SpecialValueMap()
	{
		rdfToNativeMap = new HashMap<Value, RippleValue>();
	}

	/**
	 *  @return  a native data structure which is equated with the given RDF
	 *  value.  If there is no such data structure, the value itself.  This
	 *  method will never return <code>null</code>.
	 */
	public RippleValue get( final RDFValue rdf )
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
			return new RDFValue( v );
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

    public void remove( final Value key )
    {
        rdfToNativeMap.remove( key );
    }

    public Set<Value> keySet()
	{
		return rdfToNativeMap.keySet();
	}
}
