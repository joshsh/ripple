/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/test/java/net/fortytwo/ripple/rdf/RDFUtilsTest.java $
 * $Revision: 136 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.rdf.RDFCollector;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.util.RDFUtils;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.restlet.data.MediaType;

import java.io.InputStream;

public class RDFUtilsTest extends TestCase
{
    public void testReadFromInputStream() throws Exception
    {
        InputStream is = RDFUtilsTest.class.getResourceAsStream( "rdfUtilsReadTest.ttl" );
        RDFCollector allRdf = new RDFCollector();
        SesameInputAdapter sc = new SesameInputAdapter( allRdf );
        String baseURI = "";
        RDFUtils.read( is, sc, baseURI, RDFFormat.TURTLE );
        is.close();

        Collector<Statement> allStatements = new Collector<Statement>();
        Collector<Namespace> allNamespaces = new Collector<Namespace>();
        allRdf.statementSource().writeTo( allStatements );
        allRdf.namespaceSource().writeTo( allNamespaces );

        Assert.assertEquals( 4, allStatements.size() );
        Assert.assertEquals( 3, allNamespaces.size() );
    }

    public void testFindMediaType() throws Exception {
        assertEquals("application/x-trig", RDFUtils.findMediaType(RDFFormat.TRIG).getName());
        assertEquals("application/trix", RDFUtils.findMediaType(RDFFormat.TRIX).getName());
        assertEquals("text/plain", RDFUtils.findMediaType(RDFFormat.NTRIPLES).getName());
        assertEquals("text/rdf+n3", RDFUtils.findMediaType(RDFFormat.N3).getName());
        assertEquals("application/rdf+xml", RDFUtils.findMediaType(RDFFormat.RDFXML).getName());
        assertEquals("text/turtle", RDFUtils.findMediaType(RDFFormat.TURTLE).getName());
        //assertEquals("application/x-turtle", RDFUtils.findMediaType(RDFFormat.TURTLE).getName());
    }

    public void testFindRdfFormat() throws Exception {
        assertEquals(RDFFormat.RDFXML, RDFUtils.findRdfFormat(new MediaType("application/rdf+xml")));
        assertEquals(RDFFormat.RDFXML, RDFUtils.findRdfFormat(new MediaType("application/xml")));
        assertEquals(RDFFormat.N3, RDFUtils.findRdfFormat(new MediaType("text/rdf+n3")));
        assertEquals(RDFFormat.NTRIPLES, RDFUtils.findRdfFormat(new MediaType("text/plain")));
        assertEquals(RDFFormat.TRIG, RDFUtils.findRdfFormat(new MediaType("application/x-trig")));
        assertEquals(RDFFormat.TRIX, RDFUtils.findRdfFormat(new MediaType("application/trix")));
        assertEquals(RDFFormat.TURTLE, RDFUtils.findRdfFormat(new MediaType("application/x-turtle")));
        assertEquals(RDFFormat.TURTLE, RDFUtils.findRdfFormat(new MediaType("text/turtle")));
    }
}

