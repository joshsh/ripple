/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.rdf.RDFUtils;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.linkeddata.Rdfizer;
import net.fortytwo.linkeddata.ContextMemo;
import net.fortytwo.linkeddata.WebClosure;
import net.fortytwo.linkeddata.ContextProperty;
import net.fortytwo.linkeddata.dereferencers.FileURIDereferencer;
import net.fortytwo.linkeddata.dereferencers.JarURIDereferencer;
import net.fortytwo.linkeddata.dereferencers.HTTPURIDereferencer;
import net.fortytwo.linkeddata.rdfizers.ImageRdfizer;
import net.fortytwo.linkeddata.rdfizers.VerbatimRdfizer;
import org.apache.log4j.Logger;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.Resource;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailChangedListener;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.StackableSail;
import org.restlet.data.MediaType;

import java.io.File;
import java.util.Map;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A thread-safe Sail which treats the Semantic Web as a single global graph of
 * linked data.
 */
public class LinkedDataSail implements StackableSail
{
	private static final String
            LOG_FAILED_URIS = "net.fortytwo.linkeddata.logFailedUris",
            USE_COMPACT_MEMO_FORMAT = "net.fortytwo.linkeddata.useCompactMemoFormat";

	private static final Logger LOGGER = Logger.getLogger( LinkedDataSail.class );

	// TODO: move this
	private static final String[] BADEXT = {
		"123", "3dm", "3dmf", "3gp", "8bi", "aac", "ai", "aif", "app", "asf",
		"asp", "asx", "avi", "bat", "bin", "bmp", "c", "cab", "cfg", "cgi",
		"com", "cpl", "cpp", "css", "csv", "dat", "db", "dll", "dmg", "dmp",
		"doc", "drv", "drw", "dxf", "eps", "exe", "fnt", "fon", "gif", "gz",
		"h", "hqx", "htm", "html", "iff", "indd", "ini", "iso", "java", /*"jpeg",*/
		/*"jpg",*/ "js", "jsp", "key", "log", "m3u", "mdb", "mid", "midi", "mim",
		"mng", "mov", "mp3", "mp4", "mpa", "mpg", "msg", "msi", "otf", "pct",
		"pdf", "php", "pif", "pkg", "pl", "plugin", "png", "pps", "ppt", "ps",
		"psd", "psp", "qt", "qxd", "qxp", "ra", "ram", "rar", "reg", "rm",
		"rtf", "sea", "sit", "sitx", "sql", "svg", "swf", "sys", "tar", "tif",
		"ttf", "uue", "vb", "vcd", "wav", "wks", "wma", "wmv", "wpd", "wps",
		"ws", "xhtml", "xll", "xls", "yps", "zip"};

	private static boolean logFailedUris;

	private final WebClosure webClosure;
	private final URIMap URIMap;

    private Sail baseSail;
    private boolean initialized = false;
    
    private URI
        cacheContext,
        cacheMemo;

    /**
	 * @param baseSail  (should be initialized before this object is used)
	 */
	public LinkedDataSail( final Sail baseSail, final URIMap URIMap )
		throws RippleException
	{
        RippleProperties properties = Ripple.getProperties();
        logFailedUris = properties.getBoolean( LOG_FAILED_URIS );

		this.baseSail = baseSail;
		this.URIMap = URIMap;

		webClosure = createDefaultWebClosure();
	}

	public void addSailChangedListener( final SailChangedListener listener )
	{
	}

	public synchronized SailConnection getConnection()
		throws SailException
	{
		if ( !initialized )
		{
			throw new SailException( "LinkedDataSail has not been initialized" );
		}
		
		return new LinkedDataSailConnection( baseSail, webClosure, URIMap );
	}

	public File getDataDir()
	{
return null;
	}

	public ValueFactory getValueFactory()
	{
		// Inherit the base Sail's ValueFactory
		return baseSail.getValueFactory();
	}

	public void initialize() throws SailException
	{
		ValueFactory vf = getValueFactory();
		
		cacheContext = vf.createURI( WebClosure.CACHE_CONTEXT );
		cacheMemo = vf.createURI( WebClosure.CACHE_MEMO );

		try
		{
			restoreCacheMetaData();
		}

		catch ( RippleException e )
		{
			throw new SailException( e );
		}
		
		initialized = true;
	}

	public boolean isWritable()
		throws SailException
	{
		return true;
	}

	public void removeSailChangedListener( final SailChangedListener listener )
	{
	}

	public void setDataDir( final File dataDir )
	{
	}

	// Deprecated.
	public void setParameter( final String key, final String value )
	{
	}

	public void shutDown() throws SailException
	{
        try
        {
            persistCacheMetadata();
        }

        catch ( RippleException e )
        {
            throw new SailException( e );
        }
    }

	// Extended API ////////////////////////////////////////////////////////////

	public synchronized LinkedDataSailConnection getConnection( final RDFDiffSink listenerSink )
		throws SailException
	{
		return new LinkedDataSailConnection( baseSail, webClosure, URIMap, listenerSink );
	}

public WebClosure getClosureManager()
{
	return webClosure;
}

	public Sail getBaseSail()
	{
		return baseSail;
	}
	
	public void setBaseSail( final Sail baseSail )
	{
		this.baseSail = baseSail;
	}
	
	////////////////////////////////////////////////////////////////////////////

	private WebClosure createDefaultWebClosure() throws RippleException
	{
		WebClosure wc = new WebClosure( URIMap, getValueFactory() );

		// Add URI dereferencers.
		HTTPURIDereferencer hdref = new HTTPURIDereferencer( wc );
		for ( int i = 0; i < BADEXT.length; i++ )
		{
			hdref.blackListExtension( BADEXT[i] );
		}
		wc.addDereferencer( "http", hdref );
		wc.addDereferencer( "jar", new JarURIDereferencer() );
		wc.addDereferencer( "file", new FileURIDereferencer() );

		// Add rdfizers.
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.RDFXML ), new VerbatimRdfizer( RDFFormat.RDFXML ) );
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.TURTLE ), new VerbatimRdfizer( RDFFormat.TURTLE ) );
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.N3 ), new VerbatimRdfizer( RDFFormat.N3 ), 0.9 );
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.TRIG ), new VerbatimRdfizer( RDFFormat.TRIG ), 0.8 );
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.TRIX ), new VerbatimRdfizer( RDFFormat.TRIX ), 0.8 );
		wc.addRdfizer( RDFUtils.findMediaType( RDFFormat.NTRIPLES ), new VerbatimRdfizer( RDFFormat.NTRIPLES ), 0.5 );
        Rdfizer imageRdfizer = new ImageRdfizer();
        // Mainstream EXIF-compatible image types: JPEG, TIFF
        wc.addRdfizer( MediaType.IMAGE_JPEG, imageRdfizer, 0.4 );
        wc.addRdfizer( new MediaType( "image/tiff" ), imageRdfizer, 0.4 );
        wc.addRdfizer( new MediaType( "image/tiff-fx" ), imageRdfizer, 0.4 );
        // TODO: add an EXIF-based Rdfizer for RIFF WAV audio files

        // Don't bother trying to dereference terms in these common namespaces.
		wc.addMemo( "http://www.w3.org/XML/1998/namespace#", new ContextMemo( ContextMemo.Status.Ignored ) );
		wc.addMemo( "http://www.w3.org/2001/XMLSchema", new ContextMemo( ContextMemo.Status.Ignored ) );
		wc.addMemo( "http://www.w3.org/2001/XMLSchema#", new ContextMemo( ContextMemo.Status.Ignored ) );

		// Don't try to dereference the cache index.
		wc.addMemo( "http://fortytwo.net/2007/08/ripple/cache#", new ContextMemo( ContextMemo.Status.Ignored ) );

		return wc;
	}

	/**
	 * Writes cache metadata to the base Sail.
	 * Note: for now, this metadata resides in the null context.
	 */
	public void persistCacheMetadata() throws SailException, RippleException {
        LOGGER.debug( "writing context memos" );

        boolean compact, useBNodes;
        compact = Ripple.getProperties().getBoolean( USE_COMPACT_MEMO_FORMAT );
        useBNodes = Ripple.getProperties().getBoolean( Ripple.USE_BLANK_NODES );

        synchronized ( baseSail )
		{
			ValueFactory vf = getValueFactory();
            URI fullMemo = vf.createURI( WebClosure.FULL_MEMO );
            SailConnection sc = baseSail.getConnection();

            try
            {
                // TODO: move this so that it's a default which can be overridden
                sc.setNamespace( "cache", WebClosure.CACHE_NS );

                // Clear any existing cache metadata (in any named graph).
                sc.removeStatements( null, null, null, cacheContext );
                sc.commit();

                Map<String, ContextMemo> map = webClosure.getMemos();
                for ( String k : map.keySet() )
                {
                    ContextMemo memo = map.get( k );
                    URI contextURI = vf.createURI( k );

                    // Write context memo in the compact, statement-per-context format
                    if ( compact )
                    {
                        Literal memoLit = vf.createLiteral( memo.toString() );
                        sc.addStatement( contextURI, cacheMemo, memoLit, cacheContext );
                    }

                    // Write context memo in the expanded, statement-per-property format
                    else
                    {
                        Resource memoResource = useBNodes
                                ? vf.createBNode()
                                : RDFUtils.createBNodeUri( vf );
                        sc.addStatement( contextURI, fullMemo, memoResource, cacheContext );

                        for ( ContextProperty entry : memo.getEntries() )
                        {
                            URI key = vf.createURI( WebClosure.CACHE_NS + entry.key );
                            URI datatype = entry.valueDatatype;
                            Literal value = ( null == datatype )
                                    ? vf.createLiteral( entry.value )
                                    : vf.createLiteral( entry.value, datatype );
                            sc.addStatement( memoResource, key, value, cacheContext );
                        }
                    }
                }

                sc.commit();
            }

            finally
            {
                sc.close();
            }
        }
	}

	/**
	 * Restores dereferencer state by reading success and failure memos from
	 * the last session (if present).
	 */
	private void restoreCacheMetaData() throws SailException, RippleException
	{
        LOGGER.debug( "reading context memos" );

        // TODO: determine the memo format based on the cache itself, rather than the configuration property
        boolean compact = Ripple.getProperties().getBoolean( USE_COMPACT_MEMO_FORMAT );
        
        synchronized ( baseSail )
		{
			CloseableIteration<? extends Statement, SailException> iter;
			SailConnection sc = baseSail.getConnection();

            try
            {
                // Read context memos in the compact, statement-per-context
                if ( compact )
                {
                    iter = sc.getStatements( null, cacheMemo, null, false, cacheContext );

                    try
                    {
                        while ( iter.hasNext() )
                        {
                            Statement st = iter.next();
                            URI subj = (URI) st.getSubject();
                            Literal obj = (Literal) st.getObject();

                            ContextMemo memo = new ContextMemo( obj.getLabel() );
                            webClosure.addMemo( subj.toString(), memo );
                        }
                    }

                    finally
                    {
                        iter.close();
                    }
                }

                // Read context memos in the expanded, statement-per-property format
                else
                {
                    URI fullMemo = getValueFactory().createURI( WebClosure.FULL_MEMO );
                    Collection<Statement> toMemos = new LinkedList<Statement>();
                    iter = sc.getStatements( null, fullMemo, null, false, cacheContext );

                    try
                    {
                        while ( iter.hasNext() )
                        {
                            toMemos.add( iter.next() );
                        }
                    }

                    finally
                    {
                        iter.close();
                    }

                    for ( Statement st : toMemos )
                    {
                        URI contextResource = (URI) st.getSubject();
                        Resource memoResource = (URI) st.getObject();

                        Collection<ContextProperty> entries
                                = new LinkedList<ContextProperty>();

                        iter = sc.getStatements( memoResource, null, null, false, cacheContext );

                        try
                        {
                            while ( iter.hasNext() )
                            {
                                Statement memoSt = iter.next();
                                URI pred = memoSt.getPredicate();
                                Literal obj = (Literal) memoSt.getObject();

                                ContextProperty entry = new ContextProperty();
                                entry.key = pred.getLocalName();
                                entry.value = obj.getLabel();
                                // Note: datatype is unimportant here; it's only for external applications
                                entries.add( entry );
                            }
                        }

                        finally
                        {
                            iter.close();
                        }

                        ContextMemo memo = new ContextMemo( entries );
                        webClosure.addMemo( contextResource.toString(), memo );
                    }
                }
            }

            finally
            {
                sc.close();
            }
        }
	}
	
	public static boolean logFailedUris()
	{	
		return logFailedUris;
	}
}

