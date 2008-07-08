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
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Defines a mapping between keywords and URIs, and between namespace prefixes
 * and URIs.
 */
public class Lexicon
{
    // Note: these definitions are to be kept in exact agreement with those in
    // the Ripple parser grammar.
    private static final String
            NAME_START_CHAR_NOUSC = "[A-Z]|[a-z]" +
                    "|[\u00C0-\u00D6]" +
                    "|[\u00D8-\u00F6]" +
                    "|[\u00F8-\u02FF]" +
                    "|[\u0370-\u037D]" +
                    "|[\u037F-\u1FFF]" +
                    "|[\u200C-\u200D]" +
                    "|[\u2070-\u218F]" +
                    "|[\u2C00-\u2FEF]" +
                    "|[\u3001-\uD7FF]" +
                    "|[\uF900-\uFDCF]" +
                    "|[\uFDF0-\uFFFD]",
            NAME_CHAR = NAME_START_CHAR_NOUSC +
                    "|_|-" +
                    "|\\d" +
                    "|'\u00B7'" +
                    "|['\u0300'-'\u036F']" +
                    "|['\u203F'-'\u2040']";
    private static final Pattern
            NAME_OR_PREFIX = Pattern.compile("(" + NAME_START_CHAR_NOUSC + ")(" + NAME_CHAR + ")*"),
            NAME_NOT_PREFIX = Pattern.compile("_(" + NAME_CHAR + ")*");

    private final Map<String, Set<URI>> keywordToURIMap;
	private final Map<URI, String> uriToKeywordMap;
	private final Map<String, String> prefixToNamespaceMap;
	private final Map<String, String> namespaceToPrefixMap;
	private final Collection<String> allQNames;

	public Lexicon( final Model model ) throws RippleException
	{
		prefixToNamespaceMap = new HashMap<String, String>();
		namespaceToPrefixMap = new HashMap<String, String>();
		allQNames = new ArrayList<String>();

		ModelConnection mc = model.getConnection( "for Lexicon constructor" );
        try {
            keywordToURIMap = new HashMap<String, Set<URI>>();
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

                    Set<URI> siblings = keywordToURIMap.get( keyword );

                    if ( null == siblings )
                    {
                        siblings = new HashSet<URI>();
                        keywordToURIMap.put( keyword, siblings );

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

    public boolean isValidPrefix( final String prefix )
    {
        return ( 0 == prefix.length() )
                || NAME_OR_PREFIX.matcher( prefix ).matches();
    }
    
    public boolean isValidLocalName( final String localName )
    {
        return ( 0 == localName.length() )
                || NAME_OR_PREFIX.matcher( localName ).matches()
                || NAME_NOT_PREFIX.matcher( localName ).matches();
    }

    public Set<URI> uriForKeyword( final String localName )
	{
		Set<URI> result = keywordToURIMap.get( localName );

		// If there are no results, return an empty list instead of null.
		return ( null == result )
			? new HashSet<URI>()
			: result;
	}

	public String resolveNamespacePrefix( final String nsPrefix )
	{
		return prefixToNamespaceMap.get( nsPrefix );
	}

	public String symbolForURI( final URI uri )
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
                String localName = uri.getLocalName();

                // Note: assumes that the local name is never null (although it
				//       may be empty).
                symbol = ( isValidPrefix( nsPrefix ) && isValidLocalName( localName ) )
                        ? symbol = nsPrefix + ":" + uri.getLocalName()
                        : null;
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
		Set<String> keywords = keywordToURIMap.keySet();
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

