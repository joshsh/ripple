/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import jline.Completor;
import jline.NullCompletor;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.jline.LexicalCompletor;
import org.openrdf.model.Namespace;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines a mapping between keywords and URIs, and between namespace prefixes
 * and URIs.
 */
public class Lexicon
{
	private Map<String, List<URI>> keywordToUriMap = null;
	private Map<URI, String> uriToKeywordMap = null;
	private Map<String, String> prefixToNamespaceMap = null;
	private Map<String, String> namespaceToPrefixMap = null;
	private Collection<String> allQNames = null;

	public Lexicon( final Model model ) throws RippleException
	{
		createKeywordMap( model );

		prefixToNamespaceMap = new HashMap<String, String>();
		namespaceToPrefixMap = new HashMap<String, String>();
		allQNames = new ArrayList<String>();
	}

	private void createKeywordMap( final Model model ) throws RippleException
	{
		ModelConnection mc = model.getConnection( "for Lexicon constructor" );

        try {
            keywordToUriMap = new HashMap<String, List<URI>>();
            uriToKeywordMap = new HashMap<URI, String>();

            ModelBridge bridge = model.getBridge();

            for ( Value key : bridge.keySet() )
            {
//                System.out.println("key = " + key);
                // An extra trip through the bridge replaces aliases with
                // "definitive" values.
                Value v = bridge.get( key ).toRdf( mc ).getRdfValue();

                if ( v instanceof URI )
                {
//                    System.out.println("    value = " + v);
                    
                    // By using the local name of the key value instead of the mapped-to
                    String keyword = ( (URI) key ).getLocalName();
//                    String keyword = ( (URI) v ).getLocalName();

                    List<URI> siblings = keywordToUriMap.get( keyword );

                    if ( null == siblings )
                    {
                        siblings = new ArrayList<URI>();
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
        } finally {
            mc.close();
        }
    }

	public List<URI> uriForKeyword( final String localName )
	{
		List<URI> result = keywordToUriMap.get( localName );

		// If there are no results, return an empty list instead of null.
		return ( null == result )
			? new ArrayList<URI>()
			: result;
	}

	public String resolveNamespacePrefix( final String nsPrefix )
	{
		return prefixToNamespaceMap.get( nsPrefix );
	}

	public String symbolForUri( final URI uri )
	{
		// Does it have a keyword?
		String symbol = uriToKeywordMap.get( uri );

		// If not, does it have a namespace prefix?
		if ( null == symbol )
		{
			String nsPrefix = namespaceToPrefixMap.get( uri.getNamespace() );

			// Namespace prefix may be empty but non-null.
			if ( null != nsPrefix )
			{
				// Note: assumes that the local name is never null (although it
				//       may be empty).
				symbol = nsPrefix + ":" + uri.getLocalName();
			}
		}

		return symbol;
	}

	public String nsPrefixOf( final String uri )
	{
		return namespaceToPrefixMap.get( uri );
	}

	public Completor getCompletor() throws RippleException
	{
		Set<String> keywords = keywordToUriMap.keySet();
		Set<String> prefixes = prefixToNamespaceMap.keySet();

		int size = keywords.size() + prefixes.size() + allQNames.size();
		if ( 0 < size )
		{
			Collection<String> alts = new ArrayList<String>();

			Iterator<String> localNameIter = keywords.iterator();
			while ( localNameIter.hasNext() )
			{
				alts.add( localNameIter.next() );
			}

			Iterator<String> qNameIter = allQNames.iterator();
			while ( qNameIter.hasNext() )
			{
				alts.add( qNameIter.next() );
			}

			Iterator<String> prefixIter = prefixes.iterator();
			while ( prefixIter.hasNext() )
			{
				alts.add( prefixIter.next() + ":" );
			}

			return new LexicalCompletor( alts );
		}

		else
		{
			return new NullCompletor();
		}
	}

	////////////////////////////////////////////////////////////////////////////

	public void add( final Namespace ns )
	{
//System.out.println( "(" + ns.getPrefix() + "=" + ns.getName() + ")" );
		prefixToNamespaceMap.put( ns.getPrefix(), ns.getName() );
		namespaceToPrefixMap.put( ns.getName(), ns.getPrefix() );
	}

	// Note: assumes that the same URI will not be added twice.
	public void add( final URI uri ) throws RippleException
	{
//System.out.println( "adding URI: " + uri );
		// If possible, add a qualified name as well.
		String prefix = namespaceToPrefixMap.get( uri.getNamespace() );
		if ( null != prefix )
		{
			String qName = prefix + ":" + uri.getLocalName();
//System.out.println( "adding qname: " + qName );
			allQNames.add( qName );
		}
	}
}

// kate: tab-width 4
