/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/ripple/util/HTTPUtils.java $
 * $Revision: 134 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

//To consider at some point: caching, authorization
public class HTTPUtils
{
	public static final String
			ACCEPT = "Accept",
			BODY = "Body",
			CONTENT_TYPE = "Content-Type",
			SPARQL_QUERY = "application/sparql-query",
			USER_AGENT = "User-Agent";

	private static final Map<String, Date> LAST_REQUEST_BY_HOST = new HashMap<String, Date>();

    private static long courtesyInterval;
    private static long connectionTimeout;
    private static boolean initialized = false;

    private static void initialize() throws RippleException
    {
        courtesyInterval = Ripple.getConfiguration().getLong(
                Ripple.HTTPCONNECTION_COURTESY_INTERVAL );
        connectionTimeout = Ripple.getConfiguration().getLong(
                Ripple.HTTPCONNECTION_TIMEOUT );
        initialized = true;
    }

    public static HttpClient createClient() throws RippleException
	{
        if ( !initialized )
        {
            initialize();
        }

        HttpClient client = new HttpClient();
        client.getParams().setParameter( HttpMethodParams.RETRY_HANDLER,
        		new DefaultHttpMethodRetryHandler() );
//        client.getParams().setConnectionManagerTimeout( Ripple.httpConnectionTimeout() );
        client.getParams().setParameter( "http.connection.timeout", (int) connectionTimeout );
        client.getParams().setParameter( "http.socket.timeout", (int) connectionTimeout );
        return client;
	}
	
	public static HttpMethod createGetMethod( final String url ) throws RippleException
	{
		HttpMethod method;
		
		try
		{
			method = new GetMethod( url );
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
		
		setAgent( method );
		
		return method;		
	}
	
	public static PostMethod createPostMethod( final String url ) throws RippleException
	{
		PostMethod method;
		
		try
		{
			method = new PostMethod( url );
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
		
		setAgent( method );
		
		return method;		
	}
	
	////////////////////////////////////////////////////////////////////////////

	public static HttpMethod createTextGetMethod( final String url ) throws RippleException
	{
		HttpMethod method = createGetMethod( url );
		setTextAcceptHeader( method );
		return method;
	}
	
	public static HttpMethod createRdfGetMethod( final String url ) throws RippleException
	{
		HttpMethod method = createGetMethod( url );
		setRdfAcceptHeader( method );
		return method;
	}
	
	public static PostMethod createSparqlUpdateMethod( final String url ) throws RippleException
	{
		PostMethod method = createPostMethod( url );
		setContentTypeHeader( method, SPARQL_QUERY );
		return method;
	}

	////////////////////////////////////////////////////////////////////////////

	public static void setContentTypeHeader( final HttpMethod method, final String value )
		throws RippleException
	{
		try
		{
			method.setRequestHeader( CONTENT_TYPE, value );
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}
	
	public static void setAcceptHeader( final HttpMethod method, final String value )
			throws RippleException
	{
		try
		{
			method.setRequestHeader( ACCEPT, value );
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}

	public static void setAcceptHeader( final HttpMethod method, final String [] mimeTypes )
		throws RippleException
	{
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < mimeTypes.length; i++ )
		{
			if ( i > 0 )
			{
				sb.append( ", " );
			}

			sb.append( mimeTypes[i] );
		}
		
		setAcceptHeader( method, sb.toString() );
	}
	
	public static void setTextAcceptHeader( final HttpMethod method )
			throws RippleException
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "text/plain" );
		sb.append( ", text/xml" );
		sb.append( ", text/html" );
		sb.append( ", text/plain" );
		sb.append( ", *; q=0.2" );
		sb.append( ", */*; q=0.2" );
		setAcceptHeader( method, sb.toString() );
	}
	
	public static void setRdfAcceptHeader( final HttpMethod method )
			throws RippleException
	{
		/* Comment by arjohn in http://www.openrdf.org/forum/mvnforum/viewthread?thread=805#3234
		   Note that Sesame/Rio doesn't have a real N3 parser, but it does have a Turtle parser, which supports a much larger subset of N3. At first sight, I would say that the Turtle parser should be able to parse the data fragment that you posted. */
		boolean n3DeserializationSupported = false;
	
		StringBuilder sb = new StringBuilder();
		sb.append( "application/rdf+xml" );
		if ( n3DeserializationSupported )
		{
			sb.append( ", text/rdf+n3" );
		}
		sb.append( ", application/trix" );
		sb.append( ", application/x-turtle" );
		sb.append( ", text/plain" );
		sb.append( ", application/xml;q=0.5" );
		sb.append( ", text/xml;q=0.2" );
		
		setAcceptHeader( method, sb.toString() );
	}

	////////////////////////////////////////////////////////////////////////////

	/**
	 *  A timing method which enforces crawler etiquette.
	 *  That is, it avoids the Ripple client making a nuisance of itself by
	 *  making too many requests, too quickly, of the same host.
     *  TODO: request and respect robots.txt, if present.
     */
	public static void registerMethod( final HttpMethod method ) throws RippleException
	{
        if ( !initialized )
        {
            initialize();
        }

        String host;
		
		try
		{
			host = method.getURI().getHost();
		}
		
		catch ( URIException e )
		{
			throw new RippleException( e );
		}

		// Some connections (e.g. file system operations) have no host.  Don't
		// bother regulating them.
		if ( null != host && host.length() > 0 )
		{
			Date now = new Date();
			long delay = courtesyInterval;
	
			Date lastRequest;
			long w = 0;

			synchronized (LAST_REQUEST_BY_HOST)
			{
				lastRequest = LAST_REQUEST_BY_HOST.get( host );

				// We've already made a request of this host.
				if ( null != lastRequest )
				{
					// If it hasn't been long enough since the last request from the same
					// host, wait a bit before issuing a new request.
					if ( now.getTime() - lastRequest.getTime() < delay )
					{
						w = lastRequest.getTime() + delay - now.getTime();
					}
				}

				// Record the projected start time of the request beforehand, to
				// avoid any other requests being scheduled without knowledge of
				// this one.
				LAST_REQUEST_BY_HOST.put( host, new Date( w + now.getTime() ) );
			}
	
			// Wait if necessary.
			if ( w > 0 )
			{
				try
				{
//LOGGER.info( "    waiting " + w + " milliseconds" );
					Thread.sleep( w );
				}

				catch ( InterruptedException e )
				{
					throw new RippleException( e );
				}
			}
		}		
	}
	
	/*
	public static URLConnection openConnection( final URL url )
		throws RippleException
	{
		try
		{
			return url.openConnection();
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}
	}

	public static URLConnection openConnection( final String urlStr )
		throws RippleException
	{
		URL url;

		try
		{
			url = new URL( urlStr );
		}

		catch ( java.net.MalformedURLException e )
		{
			throw new RippleException( e );
		}

		return openConnection( url );
	}

	public InputStream getInputStream( final URLConnection uc )
		throws RippleException
	{
		try
		{
			return uc.getInputStream();
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}
	}*/

	/*
	public static void showUrlConnection( final URLConnection urlConn )
	{
		Map<String, List<String>> requestProperties
			= urlConn.getRequestProperties();
		Set<String> keys = requestProperties.keySet();

		StringBuilder sb = new StringBuilder();
		sb.append( "Request properties:\n" );

		Iterator<String> keyIter = keys.iterator();
		while ( keyIter.hasNext() )
		{
			String key = keyIter.next();
			sb.append( "\t" + key + ": " );
			Iterator<String> valueIter = requestProperties.get( key ).iterator();

			boolean first = true;
			while ( valueIter.hasNext() )
			{
				String value = valueIter.next();
				if ( first )
				{
					first = false;
				}
				else
				{
					sb.append( ", " );
				}
				sb.append( value );
			}

			sb.append( "\n" );
		}

		System.out.println( sb.toString() );
	}*/

	////////////////////////////////////////////////////////////////////////////

	private static void setAgent( final HttpMethod method )
	{
		method.setRequestHeader( USER_AGENT, Ripple.getName() + "/" + Ripple.getVersion() );
	}
}

