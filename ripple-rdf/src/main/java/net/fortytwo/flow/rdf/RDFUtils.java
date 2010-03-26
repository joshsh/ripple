/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.Resource;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.restlet.resource.Variant;
import org.restlet.data.MediaType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;


public final class RDFUtils
{
	private static final Logger LOGGER = Logger.getLogger( RDFUtils.class );

    private static final Random RAND = new Random();
    
    private static Map<RDFFormat, MediaType> rdfFormatToMediaTypeMap;
	private static Map<MediaType, RDFFormat> mediaTypeToRdfFormatMap;
	private static List<Variant> rdfVariants = null;
    private static boolean dereferenceUrisByNamespace;
    private static boolean initialized = false;

    private RDFUtils()
	{
	}

    private static void initialize() throws RippleException
    {
        rdfFormatToMediaTypeMap = new HashMap<RDFFormat, MediaType>();

		// Note: preserves order of insertion
		mediaTypeToRdfFormatMap = new LinkedHashMap<MediaType, RDFFormat>();

		// Note: the first format registered becomes the default format.
		registerRdfFormat( RDFFormat.RDFXML );
		registerRdfFormat( RDFFormat.TURTLE );
		registerRdfFormat( RDFFormat.N3 );
		registerRdfFormat( RDFFormat.NTRIPLES );
		registerRdfFormat( RDFFormat.TRIG );
		registerRdfFormat( RDFFormat.TRIX );

        rdfVariants = new LinkedList<Variant>();
        for ( MediaType mediaType : mediaTypeToRdfFormatMap.keySet() )
        {
            rdfVariants.add( new Variant( mediaType ) );
        }

        dereferenceUrisByNamespace = Ripple.getProperties().getBoolean(
                Ripple.DEREFERENCE_URIS_BY_NAMESPACE );

        initialized = true;
    }

   	private static void registerRdfFormat( final RDFFormat format )
	{
		MediaType t;

		if ( RDFFormat.RDFXML == format )
		{
			t = MediaType.APPLICATION_RDF_XML;
		}

		else
		{
			t = new MediaType( format.getDefaultMIMEType() );
		}

		rdfFormatToMediaTypeMap.put( format, t );
		mediaTypeToRdfFormatMap.put( t, format );
	}

	public static List<Variant> getRdfVariants() throws RippleException
	{
		if ( !initialized )
		{
            initialize();
		}

/*
System.out.println( "getRdfVariants() --> " + rdfVariants );
Iterator<Variant> iter = rdfVariants.iterator();
while(iter.hasNext()){
Variant v = iter.next();
System.out.println( "    " + v + " -- " + v.getMediaType().getName() + " -- " + v.getMediaType().getMainType() + "/" + v.getMediaType().getSubType() );
}*/
		return rdfVariants;
	}

	public static MediaType findMediaType( final RDFFormat format ) throws RippleException
	{
		if ( !initialized )
		{
            initialize();
		}

        return rdfFormatToMediaTypeMap.get( format );
	}

	public static RDFFormat findRdfFormat( final MediaType mediaType ) throws RippleException
	{
		if ( !initialized )
		{
            initialize();
		}

        return mediaTypeToRdfFormatMap.get( mediaType );
	}

	/*public static Sail createMemoryStoreSail()
		throws RippleException
	{
		try
		{
			Sail sail = new MemoryStore();
			sail.initialize();
			return sail;
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}*/

	public static RDFFormat read( final InputStream is,
								final SesameInputAdapter sa,
								final String baseUri,
								final RDFFormat format )
		throws RippleException
	{
		try
		{
			RDFParser parser = Rio.createParser( format /*, valueFactory */ );
			parser.setRDFHandler( sa );
			parser.parse( is, baseUri );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		return format;
	}

	public static SesameOutputAdapter createOutputAdapter(
			final OutputStream out,
			final RDFFormat format )
		throws RippleException
	{
		RDFWriter writer;

		try
		{
			// Note: a comment by Jeen suggests that a new writer should be created
			//       for each use:
			//       http://www.openrdf.org/forum/mvnforum/viewthread?thread=785#3159
			writer = Rio.createWriter( format, out );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		return new SesameOutputAdapter( writer );
	}

	public static RDFFormat findFormat( final String name )
	{
		RDFFormat format;

		String s = name.toLowerCase();

		// Try not to be picky.
		if ( s.equals( "n3" ) || s.equals( "notation3" ) )
		{
			format = RDFFormat.N3;
		}

		else if ( s.equals( "ntriples" ) || s.equals( "n-triples" ) )
		{
			format = RDFFormat.NTRIPLES;
		}

		else if ( s.equals( "rdfxml" ) || s.equals( "rdf/xml" ) )
		{
			format = RDFFormat.RDFXML;
		}

		else if ( s.equals( "trig" ) )
		{
			format = RDFFormat.TRIG;
		}

		else if ( s.equals( "trix" ) )
		{
			format = RDFFormat.TRIX;
		}

		else if ( s.equals( "turtle" ) )
		{
			format = RDFFormat.TURTLE;
		}

		// Alright, be picky.
		else
		{
			// Note: this may be null.
			format = RDFFormat.valueOf( s );
		}

		return format;
	}

	// Note: examines the content type first, then the URL extension.  If all
	//       else fails, default to RDF/XML and hope for the best.
	public static RDFFormat guessRdfFormat( final String uri, final String contentType )
	{
/*
System.out.println( RDFFormat.N3.getName() + ": " + RDFFormat.N3.getMIMEType() );
System.out.println( RDFFormat.NTRIPLES.getName() + ": " + RDFFormat.NTRIPLES.getMIMEType() );
System.out.println( RDFFormat.RDFXML.getName() + ": " + RDFFormat.RDFXML.getMIMEType() );
System.out.println( RDFFormat.TRIX.getName() + ": " + RDFFormat.TRIX.getMIMEType() );
System.out.println( RDFFormat.TURTLE.getName() + ": " + RDFFormat.TURTLE.getMIMEType() );
*/
//System.out.println("contentType = " + contentType);
		String ext;
		if ( null == uri )
		{
			ext = null;
		}

		else
		{
			int lastDot = uri.lastIndexOf( '.' );
			ext = ( lastDot > 0 && lastDot < uri.length() - 1 )
				? uri.substring( lastDot + 1 )
				: null;
		}
		LOGGER.debug( "extension = " + ext );

		// Primary content type rules.
		if ( null != contentType )
		{
			// See: http://www.w3.org/TR/rdf-syntax-grammar/
			if ( contentType.contains( "application/rdf+xml" ) )
			{
				return RDFFormat.RDFXML;
			}

			// See: http://www.w3.org/DesignIssues/Notation3.html
			else if ( contentType.contains( "text/rdf+n3" ) )
			{
				return RDFFormat.N3;
			}

// See: RDFFormat.TRIX.getMIMEType()
			else if ( contentType.contains( "application/trix" ) )
			{
				return RDFFormat.TRIX;
			}

			else if ( contentType.contains( "application/x-trig" ) )
			{
				return RDFFormat.TRIG;
			}

			// See: http://www.dajobe.org/2004/01/turtle/
			else if ( contentType.contains( "application/x-turtle" ) )
			{
				return RDFFormat.TURTLE;
			}

			// This will generate a lot of false positives, but we might as well
			// *try* to parse a text document.
			else if ( contentType.contains( "text/plain" ) )
			{
				return RDFFormat.NTRIPLES;
			}
		}

		// Primary uri extension rules.
		if ( null != ext )
		{
			if ( ext.equals( "n3" ) )
			{
				return RDFFormat.N3;
			}

			else if ( ext.equals( "nt" ) )
			{
				return RDFFormat.NTRIPLES;
			}

			else if ( ext.equals( "rdf" )
					|| ext.equals( "rdfs" )
					|| ext.equals( "owl" )
					|| ext.equals( "daml" )

					// examples:
					//     http://www.aaronsw.com/about.xrdf
					//     http://www.w3.org/People/karl/karl-foaf.xrdf
					|| ext.equals( "xrdf" ) )
			{
				return RDFFormat.RDFXML;
			}

// Note: another common extension for TriX files is ".xml"
			else if ( ext.equals( "trix" ) )
			{
				return RDFFormat.TRIX;
			}

			else if ( ext.equals( "trig" ) )
			{
				return RDFFormat.TRIG;
			}

			else if ( ext.equals( "ttl" ) )
			{
				return RDFFormat.TURTLE;
			}
		}

		// Secondary content type rules.
		if ( null != contentType )
		{
			if ( contentType.contains( "application/xml" ) )
			{
				return RDFFormat.RDFXML;
			}

			// precedent: http://www.mindswap.org/2004/owl/mindswappers
			else if ( contentType.contains( "text/xml" ) )
			{
				return RDFFormat.RDFXML;
			}

			// See: http://www.w3.org/TR/rdf-testcases/#ntriples)
			// This is only a secondary rule because the text/plain MIME type
			// is so broad, and the N-Triples format so uncommon.
//            else if ( contentType.contains( "text/plain" ) )
//                return RDFFormat.NTRIPLES;
		}

		// Secondary uri extension rules.
		if ( null != ext )
		{
			// precedent:
			//     http://hometown.aol.com/chbussler/foaf/chbussler.foaf
			if ( ext.equals( "foaf" ) )
			{
				return RDFFormat.RDFXML;
			}
		}

		// Blacklisting rules.  There are some common content types which are
		// not worth trying.
		if ( null != contentType )
		{
			if ( contentType.contains( "text/html" ) )
			{
				return null;
			}
		}

		return null;
		// Last-ditch rule.
		//return RDFFormat.RDFXML;
	}

	////////////////////////////////////////////////////////////////////////////

	public static boolean isHttpUri( final URI uri )
	{
		return uri.toString().startsWith( "http://" );
	}

	public static URI inferContextURI( final Resource subject,
                                       final ValueFactory valueFactory )
		throws RippleException
	{
        if ( !( subject instanceof URI ) )
        {
            return null;
        }

        else
        {
            String s = removeFragmentIdentifier( ( (URI) subject ).toString() );

            try
            {
                return valueFactory.createURI( s );
            }

            catch ( Throwable t )
            {
                throw new RippleException( t );
            }
        }
    }

	/**
	 *  @param uri  the URI to be resolved
     * @return  a String representation of the URL to be resolved
	 */
	public static String removeFragmentIdentifier( final String uri )
	{
        int i = uri.lastIndexOf('#');
        return 0 <= i ? uri.substring(0, i) : uri;
	}

    public static URI createBNodeUri( final String id, final ValueFactory vf ) throws RippleException
	{
		try
		{
			return vf.createURI( Ripple.URN_BNODE_PREFIX + id );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}

	public static URI createBNodeUri( final ValueFactory vf ) throws RippleException
	{
		// Local name will be a UUID (without the dashes).
		byte[] bytes = new byte[32];

		// Artificially constrain the fist character to be a letter, so the
		// local part of the URI is N3-friendly.
		bytes[0] = (byte) ( 'a' + RAND.nextInt( 5 ) );

		// Remaining characters are hexadecimal digits.
		for ( int i = 1; i < 32; i++ )
		{
			int c = RAND.nextInt( 16 );
			bytes[i] = (byte) ( ( c > 9 ) ? c - 10 + 'a' : c + '0' );
		}

		return createBNodeUri( new String( bytes ), vf );
	}
}
