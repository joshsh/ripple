/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import java.net.URL;

import net.fortytwo.ripple.rdf.RDFCollector;
import net.fortytwo.ripple.rdf.SesameInputAdapter;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.RDFHTTPUtils;

import org.openrdf.rio.RDFFormat;

public class RDFFormatsTest extends RippleTestCase
{
	RDFCollector collector;
	SesameInputAdapter sesameAdapter;

	private class ParseN3Test extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.n3" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.N3 );
			assertEquals( collector.countStatements(), 4 );
			assertEquals( collector.countNamespaces(), 2 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	private class ParseNtriplesTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.nt" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.NTRIPLES );
			assertEquals( collector.countStatements(), 3 );
			assertEquals( collector.countNamespaces(), 0 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	private class ParseRdfXmlTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.rdf" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.RDFXML );
			assertEquals( collector.countStatements(), 4 );
			assertEquals( collector.countNamespaces(), 3 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	private class ParseTrigTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.trig" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.TRIG );
			assertEquals( collector.countStatements(), 6 );
			assertEquals( collector.countNamespaces(), 2 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	private class ParseTrixTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.trix" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.TRIX );
			assertEquals( collector.countStatements(), 3 );
			assertEquals( collector.countNamespaces(), 0 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	private class ParseTurtleTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			collector.clear();

			URL url = RDFFormatsTest.class.getResource( "rdfformatstest.ttl" );
			RDFFormat format = RDFHTTPUtils.read( url, sesameAdapter, url.toString() );

			assertEquals( format, RDFFormat.TURTLE );
			assertEquals( collector.countStatements(), 3 );
			assertEquals( collector.countNamespaces(), 3 );
//			assertEquals( collector.countComments(), 0 );
		}
	}

	public void runTests()
		throws Exception
	{
		collector = new RDFCollector();
		sesameAdapter = new SesameInputAdapter( collector );

		testSynchronous( new ParseN3Test() );
		testSynchronous( new ParseNtriplesTest() );
		testSynchronous( new ParseRdfXmlTest() );
// FIXME
//		testSynchronous( new ParseTrigTest() );
		testSynchronous( new ParseTrixTest() );
		testSynchronous( new ParseTurtleTest() );
	}
}

