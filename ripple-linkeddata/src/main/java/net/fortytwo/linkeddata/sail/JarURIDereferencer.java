/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.rdf.RDFUtils;
import org.openrdf.rio.RDFFormat;
import org.restlet.resource.Representation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class JarURIDereferencer implements URIDereferencer
{
	public Representation dereference( final String uri ) throws RippleException
	{
		return new JarRepresentation( uri );
	}

	private class JarRepresentation extends Representation
	{
		private InputStream inputStream;

		public JarRepresentation( final String uri ) throws RippleException
		{
			RDFFormat format = RDFUtils.guessRdfFormat( uri, null );
			/*if ( null == format )
			{
				throw new RippleException( "could not guess format for URI: " + uri );
			}*/
			setMediaType( RDFUtils.findMediaType( format ) );

			JarURLConnection jc;

			try
			{
//System.out.println( "uri = " + uri );
				jc = (JarURLConnection) ( new URL( uri ).openConnection() );
				inputStream = jc.getInputStream();
			}

			catch ( IOException e )
			{
				throw new RippleException( e );
			}
		}

		public ReadableByteChannel getChannel() throws IOException {
			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}

		public InputStream getStream() throws IOException {
			return inputStream;
		}

		public void write(OutputStream outputStream) throws IOException {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		public void write(WritableByteChannel writableByteChannel) throws IOException {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
