/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

import net.fortytwo.ripple.RippleException;

import org.openrdf.model.URI;
import org.openrdf.model.Value;

public class ModelBridge
{
    // This map is order-preserving such that the most recently-added keys
    // appear first in the keySet.  This is important for Lexicon, in which
    // a keyword maps to the most recently-added URI.
    private final LinkedHashMap<Value, RippleValue> rdfToNativeMap;

	public ModelBridge()
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
		RippleValue rpl =  rdfToNativeMap.get( rdf.getRdfValue() );

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

	public void add( final RippleValue key, final RippleValue value, final ModelConnection mc )
		throws RippleException
	{
		rdfToNativeMap.put( key.toRdf( mc ).getRdfValue(), value );
	}

	public void add( final RippleValue v, final ModelConnection mc )
		throws RippleException
	{
		rdfToNativeMap.put( v.toRdf( mc ).getRdfValue(), v );
	}
	
	public Set<Value> keySet()
	{
		return rdfToNativeMap.keySet();
	}
	
	/*
	public void createKeywordMap( final ModelConnection mc ) throws RippleException
	{
		Map<String, Collection<RippleValue>> keywordToValueMap
			= new HashMap<String, Collection<RippleValue>>();

		Iterator<Value> keys = rdfToNativeMap.keySet().iterator();
		while ( keys.hasNext() )
		{
			// An extra trip through the bridge replaces aliases with
			// "definitive" values.
			Value v = get( keys.next() ).toRdf( mc ).getRdfValue();

			if ( v instanceof URI )
			{
				String keyword = ( (URI) v ).getLocalName();

				Collection<RippleValue> siblings = keywordToValueMap.get( keyword );
		
				
				if ( null == siblings )
				{
					siblings = new LinkedList<RippleValue>();
					keywordToUriMap.put( keyword, siblings );

					uriToKeywordMap.put( (URI) v, keyword );
				}

				// The presence of aliases will cause the same URI / keyword
				// pair to appear more than once.
				if ( !siblings.contains( (URI) v ) )
				{
					siblings.add( (URI) v );
				}
			}
		}
	}*/
}
