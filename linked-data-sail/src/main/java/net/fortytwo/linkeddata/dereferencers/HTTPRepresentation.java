/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-linkeddata/src/main/java/net/fortytwo/linkeddata/sail/HTTPRepresentation.java $
 * $Revision: 6 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.util.HTTPUtils;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Author: josh
 * Date: Jan 16, 2008
 * Time: 3:46:20 PM
 */
public class HTTPRepresentation extends Representation
{
    private final InputStream inputStream;
    private final HttpMethod method;

    // Note: the URI is immediately dereferenced
    public HTTPRepresentation( final String uri, final String acceptHeader ) throws RippleException
    {
        method = HTTPUtils.createGetMethod( uri );
		HTTPUtils.setAcceptHeader( method, acceptHeader );
		HTTPUtils.registerMethod( method );

		HttpClient client = HTTPUtils.createClient();

		try
		{
			client.executeMethod( method );
		}

		catch ( ConnectTimeoutException e )
		{
			
		}

		catch ( IOException e )
		{
			throw new RippleException( e );
		}

		InputStream is;

		try
		{
			is = method.getResponseBodyAsStream();
		}

		catch ( IOException e )
		{
			throw new RippleException( e );
		}

        if ( null == is )
        {
            throw new RippleException( "null input stream" );
        }

		inputStream = new HttpRepresentationInputStream( is );

        Header h = method.getResponseHeader( HTTPUtils.CONTENT_TYPE );
        if ( null == h )
        {
            throw new RippleException( "no content-type header served for resource <"
                    + StringUtils.escapeURIString( uri ) + ">" );                    
        }

        String mtStr = h.getValue().split( ";" )[0];
		if ( null == mtStr || 0 == mtStr.length() )
		{
			throw new RippleException( "no media type found for resource <"
					+ StringUtils.escapeURIString( uri ) + ">" );
		}
		MediaType mt = new MediaType( mtStr );
//System.out.println( "discovered media type is: " + mt );
		setMediaType( mt );
    }

    public ReadableByteChannel getChannel() throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public InputStream getStream() throws IOException
    {
        return inputStream;
    }

    public void write( final OutputStream outputStream ) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void write( final WritableByteChannel writableByteChannel ) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * A helper InputStream which closes the HTTP connection when it itself is
	 * closed
     */
    private class HttpRepresentationInputStream extends InputStream
    {
        private InputStream innerInputStream;

        public HttpRepresentationInputStream( final InputStream is )
        {
            innerInputStream = is;
        }

        public int read() throws IOException
        {
            return innerInputStream.read();
        }
        
        @Override
        public void close() throws IOException
        {
            method.releaseConnection();

            innerInputStream.close();
        }

		@Override
		public int available() throws IOException
		{
			return innerInputStream.available();
		}
	}
}
