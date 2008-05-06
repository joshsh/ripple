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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Defines a mapping between keywords and URIs, and between namespace prefixes
 * and URIs.
 */
public class Lexicon
{
	private Map<String, Set<URI>> keywordToUriMap = null;
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
            keywordToUriMap = new HashMap<String, Set<URI>>();
            uriToKeywordMap = new HashMap<URI, String>();

            // Note: the order of the "set" of special values is significant.
            // Specifically, all primary values are expected to come before all
            // aliases.
            for ( Value key : model.getSpecialValues() )
            {
                if ( key instanceof URI )
                {
                    Value mapsTo = mc.value( key ).toRDF( mc ).sesameValue();
                    boolean isPrimary = key.equals( mapsTo );

                    String keyword = ( (URI) key ).getLocalName();

                    Set<URI> siblings = keywordToUriMap.get( keyword );

                    if ( null == siblings )
                    {
                        siblings = new HashSet<URI>();
                        keywordToUriMap.put( keyword, siblings );

                        uriToKeywordMap.put( (URI) key, keyword );
                    }

                    // Add the value if it is a primary value (possibly
                    // overriding a previously-added alias value) or if it is an
                    // alias value which does not conflict with the
                    // corresponding primary value.
                    if ( isPrimary || !siblings.contains( mapsTo ) )
                    {
                        siblings.add( (URI) key );
                    }
                }
            }
        } finally {
            mc.close();
        }
    }

	public Set<URI> uriForKeyword( final String localName )
	{
		Set<URI> result = keywordToUriMap.get( localName );

		// If there are no results, return an empty list instead of null.
		return ( null == result )
			? new HashSet<URI>()
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

