/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.rdf.BNodeToURIFilter;
import net.fortytwo.ripple.rdf.RDFBuffer;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.RDFUtils;
import net.fortytwo.ripple.rdf.SesameInputAdapter;
import net.fortytwo.ripple.rdf.SingleContextPipe;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.RDFHandler;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Note: this tool stores metadata about web activity; if a suitable
 * dereferencer cannot be found for a URI, no metadata will be stored.
 * 
 * Author: josh
 * Date: Jan 16, 2008
 * Time: 12:25:29 PM
 */
public class WebClosure  // TODO: the name is a little misleading...
{
	// TODO: these should probably not be HTTP URIs
	public static final String
            CACHE_NS = "http://fortytwo.net/2008/01/webclosure#",
            CACHE_CONTEXT = "http://fortytwo.net/2008/01/webclosure#context",
            CACHE_MEMO = "http://fortytwo.net/2008/01/webclosure#memo",
            FULL_MEMO = "http://fortytwo.net/2008/01/webclosure#fullMemo";

	private static final Logger LOGGER = Logger.getLogger( WebClosure.class );

	private Map<String, ContextMemo> memos = new HashMap<String, ContextMemo>();

	// Maps media types to Rdfizers
	private Map<MediaType, MediaTypeInfo> rdfizers
			= new HashMap<MediaType, MediaTypeInfo>();

	// Maps URI schemes to Dereferencers
	private Map<String, Dereferencer> dereferencers = new HashMap<String, Dereferencer>();

    private URIMap uriMap;
	private ValueFactory valueFactory;
    private boolean useBlankNodes;

    private String acceptHeader = null;

	public WebClosure( final URIMap uriMap, final ValueFactory vf ) throws RippleException
	{
        this.uriMap = uriMap;
		valueFactory = vf;
        useBlankNodes = Ripple.getProperties().getBoolean(Ripple.USE_BLANK_NODES);
    }

	public String getAcceptHeader()
	{
		if ( null == acceptHeader )
		{
 			StringBuilder sb = new StringBuilder();
			boolean first = true;

			// Order from highest quality to lowest.
			Comparator<MediaTypeInfo> comparator
					= new Comparator<MediaTypeInfo>()
			{
				public int compare( final MediaTypeInfo first,
									final MediaTypeInfo second )
				{
					return first.quality < second.quality ? 1 : first.quality > second.quality ? -1 : 0;
				}
			};

			MediaTypeInfo[] array = new MediaTypeInfo[rdfizers.size()];
			rdfizers.values().toArray( array );
			Arrays.sort( array, comparator );

			for ( MediaTypeInfo m : array )
			{
				if ( first )
				{
					first = false;
				}

				else
				{
					sb.append(", ");
				}

				sb.append( m.mediaType.getName() );
				double quality = m.quality;
				if ( 1.0 != quality )
				{
					sb.append( ";q=" ).append( quality );
				}
			}

			acceptHeader = sb.toString();
		}

		return acceptHeader;
	}
	
	public void addRdfizer( final MediaType mediaType,
							final Rdfizer rdfizer,
							final double qualityFactor )
	{
		if ( qualityFactor <= 0 || qualityFactor > 1 )
		{
			throw new IllegalArgumentException( "quality factor must be between 0 and 1" );
		}

		MediaTypeInfo rq = new MediaTypeInfo();
		rq.mediaType = mediaType;
		rq.quality = qualityFactor;
		rq.rdfizer = rdfizer;
		rdfizers.put( mediaType, rq );

		acceptHeader = null;
	}

	public void addRdfizer( final MediaType mediaType, final Rdfizer rdfizer )
	{
		addRdfizer( mediaType, rdfizer, 1.0 );
	}

	public void addDereferencer( final String scheme, final Dereferencer uriDereferencer )
	{
		dereferencers.put( scheme, uriDereferencer );
	}

	public void addMemo( final String uri, final ContextMemo memo )
	{
		memos.put( uri, memo );
	}

	public Map<String, ContextMemo> getMemos()
	{
		return memos;
	}

	public ContextMemo.Status extend( final URI uri, final RDFSink resultSink ) throws RippleException
	{
		ContextMemo.Status status = extendPrivate( uri, resultSink );

		return status;
	}

	private ContextMemo.Status logStatus( final URI uri, final ContextMemo.Status status )
	{
		if ( ContextMemo.Status.Success != status )
		{
			LOGGER.info( "Failed to dereference URI <"
					+ StringUtils.escapeUriString( uri.toString() ) + ">: " + status );
		}

		return status;
	}

	private ContextMemo.Status extendPrivate( final URI uri, final RDFSink resultSink ) throws RippleException
	{
		// TODO: memos should be inferred in a scheme-specific way
		String memoUri = RDFUtils.inferContext( uri );

		ContextMemo memo;
		Dereferencer dref;

		// Note: this URL should be treated as a "black box" once created; it
		// need not resemble the URI it was created from.
		String mapped;

		// Rules out an otherwise possible race condition
		synchronized ( memos )
		{
			memo = memos.get( memoUri );

			if ( null != memo )
			{
				// Don't log success or failure based on cached values.
				return memo.getStatus();
			}

			try
			{
				mapped = uriMap.get( memoUri );
			}

			catch ( RippleException e )
			{
				// Don't log extremely common errors.
				return ContextMemo.Status.InvalidUri;
			}

			try
			{
				dref = chooseDereferencer( mapped );
			}

			catch ( RippleException e )
			{
				e.logError( false );

				// Don't log extremely common errors.
				return ContextMemo.Status.InvalidUri;
			}

			if ( null == dref )
			{
				// Don't log extremely common errors.
				return ContextMemo.Status.BadUriScheme;
			}

			LOGGER.info( "Dereferencing URI <"
					+ StringUtils.escapeUriString( uri.toString() ) + ">" );
					//+ " at location " + mapped );

			memo = new ContextMemo( ContextMemo.Status.Success );
			memos.put( memoUri, memo );
		}

		memo.setUriDereferencer( dref );

		// Note: from this point on, failures are explicitly stored as caching
		// metadata.

		Representation rep;

		try
		{
			rep = dref.dereference( mapped );
		}

		catch ( RippleException e )
		{
			e.logError();
			memo.setStatus( ContextMemo.Status.DereferencerError );
			return logStatus( uri, memo.getStatus() );
		}

		catch ( Throwable t )
		{
			memo.setStatus( ContextMemo.Status.DereferencerError );
			logStatus( uri, memo.getStatus() );
			throw new RippleException( t );
		}

		MediaType mt;

		try
		{
			mt = rep.getMediaType();
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
		
//System.out.println( "media type = " + mt );
		memo.setMediaType( mt );

		Rdfizer rfiz = chooseRdfizer( mt );
		if ( null == rfiz )
		{
			memo.setStatus( ContextMemo.Status.BadMediaType );
			memo.setMediaType( mt );
			return logStatus( uri, memo.getStatus() );
		}

		memo.setRdfizer( rfiz );

		URI context;

		try
		{
			context = valueFactory.createURI( memoUri );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		// Note: any pre-existing context information is discarded.
		RDFSink scp = new SingleContextPipe( resultSink, context, valueFactory );
		
		RDFBuffer results = new RDFBuffer( scp );
		RDFHandler hdlr = new SesameInputAdapter( useBlankNodes
				? results
				: new BNodeToURIFilter( results, valueFactory ) );

		InputStream is;

		try
		{
			is = rep.getStream();
		}

		catch ( IOException e )
		{
			throw new RippleException( e );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		// For now...
		String baseUri = memoUri;

		ContextMemo.Status status;

		status = rfiz.handle( is, hdlr, uri, baseUri );

		if ( ContextMemo.Status.Success == status )
		{
			// Push results and record success
			results.flush();
		}

		memo.setStatus( status );

		return logStatus( uri, status );
	}

	private Dereferencer chooseDereferencer( final String uri ) throws RippleException
	{
		String scheme;

		try
		{
			scheme = new java.net.URI( uri ).getScheme();
		}

		catch ( URISyntaxException e )
		{
			throw new RippleException( e );
		}

		return dereferencers.get( scheme );
	}

	private Rdfizer chooseRdfizer( final MediaType mediaType ) throws RippleException
	{
		MediaTypeInfo rq = rdfizers.get( mediaType );
		return ( null == rq ) ? null : rq.rdfizer;
	}

	private class MediaTypeInfo
	{
		MediaType mediaType;
		public double quality;
		public Rdfizer rdfizer;
	}
}
