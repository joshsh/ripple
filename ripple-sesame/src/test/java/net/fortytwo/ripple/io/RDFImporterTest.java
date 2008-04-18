/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.net.URL;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.util.RDFHTTPUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.rdf.SesameInputAdapter;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.rdf.RDFUtils;

import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;

public class RDFImporterTest extends RippleTestCase
{
	private class ImporterTest extends TestRunnable
	{
		void addGraph( final InputStream is,
						final URI context,
						final RDFFormat format,
						final ModelConnection mc )
			throws RippleException
		{
			RDFImporter importer = new RDFImporter( mc, context );
			SesameInputAdapter sc = new SesameInputAdapter( importer );
			RDFUtils.read( is, sc, context.toString(), format );
mc.commit();
		}

		void addGraph( final URL url,
						final URI context,
						final RDFFormat format,
						final ModelConnection mc )
			throws RippleException
		{
			RDFImporter importer = new RDFImporter( mc, context );
			SesameInputAdapter sc = new SesameInputAdapter( importer );
			RDFHTTPUtils.read( url, sc, context.toString(), format );
mc.commit();
		}

		public void test()
			throws Exception
		{
			ModelConnection mc = getTestModel().getConnection( "for ImporterTest" );

			{
				URI ctxA = mc.createUri( "urn:org.example.test.addGraphTest.turtleStrA#" );

				String s = "@prefix foo:  <http://example.org/foo#>.\n"
					+ "foo:a foo:b foo:c." ;
				InputStream is = new ByteArrayInputStream( s.getBytes() );

				addGraph( is, ctxA, RDFFormat.TURTLE, mc );

				//assertEquals( mc.countStatements( null ), 1 );
				assertEquals( mc.countStatements( ctxA ), 1 );
			}

			{
				URL test1Url = RDFImporterTest.class.getResource( "rdfImporterTest1.ttl" );
				URL test2Url = RDFImporterTest.class.getResource( "rdfImporterTest2.ttl" );

				URI ctxA = mc.createUri( "urn:org.example.test.addGraphTest.turtleA#" );
				URI ctxB = mc.createUri( "urn:org.example.test.addGraphTest.turtleB#" );

				addGraph( test1Url, ctxA, RDFFormat.TURTLE, mc );
				assertEquals( mc.countStatements( ctxA ), 2 );
				addGraph( test2Url, ctxA, RDFFormat.TURTLE, mc );
				assertEquals( mc.countStatements( ctxA ), 4 );

				addGraph( test1Url, ctxB, RDFFormat.TURTLE, mc );
				assertEquals( mc.countStatements( ctxB ), 2 );
				addGraph( test2Url, ctxB, RDFFormat.TURTLE, mc );
				assertEquals( mc.countStatements( ctxB ), 4 );
			}

			{
				URL test1Url = RDFImporterTest.class.getResource( "rdfImporterTest1.rdf" );
				URL test2Url = RDFImporterTest.class.getResource( "rdfImporterTest2.rdf" );

				URI ctxA = mc.createUri( "urn:org.example.test.addGraphTest.rdfxmlA#" );
				URI ctxB = mc.createUri( "urn:org.example.test.addGraphTest.rdfxmlB#" );

				addGraph( test1Url, ctxA, RDFFormat.RDFXML, mc );
				assertEquals( mc.countStatements( ctxA ), 2 );
				addGraph( test2Url, ctxA, RDFFormat.RDFXML, mc );
				assertEquals( mc.countStatements( ctxA ), 4 );

				addGraph( test1Url, ctxB, RDFFormat.RDFXML, mc );
				assertEquals( mc.countStatements( ctxB ), 2 );
				addGraph( test2Url, ctxB, RDFFormat.RDFXML, mc );
				assertEquals( mc.countStatements( ctxB ), 4 );
			}

			mc.close();
		}
	}

	public void runTests()
		throws Exception
	{
//See: http://www.openrdf.org/issues/browse/SES-358?watch=true
		testSynchronous( new ImporterTest() );
	}
}

// kate: tab-width 4
