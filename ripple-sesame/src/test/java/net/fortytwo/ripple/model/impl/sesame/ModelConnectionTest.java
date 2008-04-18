/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.net.URL;

import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.model.ModelConnection;

import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;

public class ModelConnectionTest extends RippleTestCase
{
	private class CreateUriTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			ModelConnection mc = getTestModel().getConnection( "for CreateUriTest" );

			URI uri;
			String localName, namespace;

			// Hash namespaces.

			uri = mc.createUri( "http://example.org/foo#bar" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "bar" );
			assertEquals( namespace, "http://example.org/foo#" );

			uri = mc.createUri( "http://example.org/foo#" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "" );
			assertEquals( namespace, "http://example.org/foo#" );

			uri = mc.createUri( "http://example.org/ns/foo/#bar" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "bar" );
			assertEquals( namespace, "http://example.org/ns/foo/#" );

			uri = mc.createUri( "http://example.org/ns/foo/#" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "" );
			assertEquals( namespace, "http://example.org/ns/foo/#" );

			// Slash namespaces.

			uri = mc.createUri( "http://example.org/ns/foo/bar" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "bar" );
			assertEquals( namespace, "http://example.org/ns/foo/" );

			uri = mc.createUri( "http://example.org/ns/foo/" );
			localName = uri.getLocalName();
			namespace = uri.getNamespace();
			assertEquals( localName, "" );
			assertEquals( namespace, "http://example.org/ns/foo/" );

			mc.close();
		}
	}

/*
	private class CountStatementsTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			ModelConnection mc = getTestModel().getConnection( "for CountStatementsTest" );

			URI context = mc.createUri( "urn:org.example.test.countStatementsTest#" );

			URI uri1 = mc.createUri( "urn:org.example.test#uri1" );
			URI uri2 = mc.createUri( "urn:org.example.test#uri2" );
			URI uri3 = mc.createUri( "urn:org.example.test#uri3" );

			URI [] uris = {uri1, uri2, uri3};
			for ( int i = 0; i < 3; i++ )
				for ( int j = 0; j < 3; j++ )
					for ( int k = 0; k < 3; k++ )
						mc.add(
				new RdfValue( uris[i] ),
				new RdfValue( uris[j] ),
				new RdfValue( uris[k] ), context );
mc.commit();

			long count = mc.countStatements( context );
			assertEquals( count, 27 );

			mc.close();
		}
	}

	private class AddStatementsTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			ModelConnection mc = getTestModel().getConnection( "for AddStatementsTest" );

			URI ctxA = mc.createUri( "urn:org.example.test.addStatementsTest.ctxA#" );
			URI uri1 = mc.createUri( "urn:org.example.test.addStatementsTest.ctxA#uri1" );
			assertEquals( mc.countStatements( ctxA ), 0 );
			mc.add(
				new RdfValue( uri1 ),
				new RdfValue( uri1 ),
				new RdfValue( uri1 ) );
mc.commit();
			assertEquals( mc.countStatements( ctxA ), 1 );
		}
	}
*/

	public void runTests()
		throws Exception
	{
// Note: Sesame repositories have not responded well to asynchronous testing, so ModelConnection will have to add its own synchronization code (synchronize on the Repository).
		testSynchronous( new CreateUriTest() );
//		testSynchronous( new CountStatementsTest() );
	}
}

// kate: tab-width 4
