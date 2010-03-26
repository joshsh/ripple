/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-linkeddata/src/main/java/net/fortytwo/linkeddata/sail/HTTPURIDereferencer.java $
 * $Revision: 6 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.linkeddata.Dereferencer;
import net.fortytwo.linkeddata.WebClosure;
import net.fortytwo.linkeddata.dereferencers.HTTPRepresentation;
import org.apache.log4j.Logger;
import org.restlet.resource.Representation;

import java.util.HashSet;
import java.util.Set;

// Note: throughout this implementation, both the caching context of a URI and
//       its associated web location are the same as its success or failure 'memo'.
public class HTTPURIDereferencer implements Dereferencer
{
	private static final Logger LOGGER = Logger.getLogger( HTTPURIDereferencer.class );

	// FIXME: temporary
	private final WebClosure webClosure;

	private final Set<String> badExtensions;

	public HTTPURIDereferencer( final WebClosure webClosure )
	{
		this.webClosure = webClosure;

		badExtensions = new HashSet<String>();

	}

	public Representation dereference( final String uri ) throws RippleException
	{
	    // Don't dereference a URI which appears to point to a file which is not
		// an RDF document.
		int l = uri.lastIndexOf( '.' );
		if ( l >= 0 && badExtensions.contains( uri.substring( l + 1 ) ) )
		{
			throw new RippleException( "URI <" + StringUtils.escapeURIString( uri ) + "> has blacklisted extension" );
			// TODO: we can throw exceptions or return nulls to indicate an error, but we shouldn't do both
//			return null;
		}

        return new HTTPRepresentation( uri, webClosure.getAcceptHeader() );
	}

	public void blackListExtension( final String ext )
	{
		badExtensions.add( ext );
	}

	public void whitelistExtension( final String ext )
	{
		badExtensions.remove( ext );
	}
}

