/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.SailInserter;
import net.fortytwo.ripple.rdf.SesameInputAdapter;
import net.fortytwo.ripple.rdf.SesameOutputAdapter;
import net.fortytwo.ripple.rdf.SingleContextPipe;
import net.fortytwo.ripple.test.RippleTestCase;
import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.Literal;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SesameTest extends RippleTestCase
{
	private static final Logger LOGGER = Logger.getLogger( SesameTest.class );

	static int countStatements( final SailConnection rc, final URI context )
		throws Exception
	{
		int count = 0;

		CloseableIteration<? extends Statement, SailException> stmtIter
			= ( null == context )
				? rc.getStatements( null, null, null, false )
				: rc.getStatements( null, null, null, false, context );

		while ( stmtIter.hasNext() )
		{
			stmtIter.next();
			count++;
		}

		stmtIter.close();

		return count;
	}

    public void testRecoverFromParseError() throws Exception
    {
        Sail sail = new MemoryStore();
        sail.initialize();

        String bad = "bad" ;
        String good = "@prefix foo:  <http://example.org/foo#>.\n"
            + "foo:a foo:b foo:c." ;

        InputStream is = null;

        try {
            is = new ByteArrayInputStream( bad.getBytes() );
            add( sail, is, "", RDFFormat.TURTLE );
        } catch ( Exception e ) {}
        is.close();

        try {
            is = new ByteArrayInputStream( good.getBytes() );
            add( sail, is, "", RDFFormat.TURTLE );
        } catch ( Exception e ) {}
        is.close();

        SailConnection sc = sail.getConnection();
        int count = countStatements( sc, null );
        sc.close();

        assertEquals( 1, count );
    }

    public void testAddFromInputStream() throws Exception
    {
        Sail sail = new MemoryStore();
        sail.initialize();
        SailConnection sc = sail.getConnection();

        URI ctxA = sail.getValueFactory().createURI( "urn:test.AddFromInputStreamTest.ctxA#" );

        String s = "@prefix foo:  <http://example.org/foo#>.\n"
            + "foo:a foo:b foo:c." ;
        InputStream is = new ByteArrayInputStream( s.getBytes() );

        add( sail, is, ctxA.toString(), RDFFormat.TURTLE, ctxA );
        is.close();

        assertEquals( 1, countStatements( sc, null ) );
/* 60 */    assertEquals( 1, countStatements( sc, ctxA ) );

        sc.close();
        sail.shutDown();
    }

    // Verifies that Sesame does not unescape literal labels (not that one would
    // reasonably suspect it of doing so).
    public void testEscapeCharactersInLiterals() throws Exception
    {
        Sail sail = new MemoryStore();
        sail.initialize();
        ValueFactory vf = sail.getValueFactory();
        Literal l;

        l = vf.createLiteral( "\"" );
        assertEquals( 1, l.getLabel().length() );
        l = vf.createLiteral( "\\\"" );
        assertEquals( 2, l.getLabel().length() );

        l = vf.createLiteral( "\"", XMLSchema.STRING );
        assertEquals( 1, l.getLabel().length() );
        l = vf.createLiteral( "\\\"", XMLSchema.STRING );
        assertEquals( 2, l.getLabel().length() );

        sail.shutDown();
    }

    private void add( final Sail sail, final InputStream is, final String baseUri, final RDFFormat format ) throws Exception
	{
		RDFParser parser = Rio.createParser( format );
		SailConnection sc = sail.getConnection();
		SailInserter inserter = new SailInserter( sc );
		parser.setRDFHandler( inserter );
		
		inserter.startRDF();

		try
		{
			parser.parse( is, baseUri );
		}

		catch ( Exception e )
		{
			inserter.endRDF();
			sc.close();
			throw e;
		}

		inserter.endRDF();
		sc.commit();
		sc.close();
	}

	private void add( final Sail sail, final InputStream is, final String baseUri, final RDFFormat format, final URI context ) throws Exception
	{
		RDFParser parser = Rio.createParser( format );
		SailConnection sc = sail.getConnection();
		SailInserter inserter = new SailInserter( sc );
		SesameOutputAdapter outAdapter = new SesameOutputAdapter( inserter );
		RDFSink scp = new SingleContextPipe( outAdapter, context, sail.getValueFactory() );
		SesameInputAdapter inAdapter = new SesameInputAdapter( scp );
		parser.setRDFHandler( inAdapter );

		inserter.startRDF();
		
		try
		{
			parser.parse( is, baseUri );
		}

		catch ( Exception e )
		{
			inserter.endRDF();
			sc.close();
			throw e;
		}

		inserter.endRDF();
		sc.commit();
		sc.close();
	}
}

