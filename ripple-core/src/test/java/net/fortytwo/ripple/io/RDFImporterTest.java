/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.RDFHTTPUtils;
import net.fortytwo.ripple.util.RDFUtils;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

public class RDFImporterTest extends RippleTestCase
{
    private void addGraph( final InputStream is,
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

    private void addGraph( final URL url,
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

    private long countStatements(final ModelConnection mc,
                                final Resource... contexts) throws RippleException {
        return ((SesameModelConnection) mc).countStatements(contexts);
    }

    public void testImporter() throws Exception
    {
        ModelConnection mc = getTestModel().createConnection();

        {
            URI ctxA = createURI( "urn:org.example.test.addGraphTest.turtleStrA#", mc );

            String s = "@prefix foo:  <http://example.org/foo#>.\n"
                + "foo:a foo:b foo:c." ;
            InputStream is = new ByteArrayInputStream( s.getBytes() );

            addGraph( is, ctxA, RDFFormat.TURTLE, mc );

            //assertEquals( mc.countStatements( null ), 1 );
            assertEquals( countStatements( mc, ctxA ), 1 );
        }

        {
            URL test1Url = RDFImporterTest.class.getResource( "rdfImporterTest1.ttl" );
            URL test2Url = RDFImporterTest.class.getResource( "rdfImporterTest2.ttl" );

            URI ctxA = createURI( "urn:org.example.test.addGraphTest.turtleA#", mc );
            URI ctxB = createURI( "urn:org.example.test.addGraphTest.turtleB#", mc );

            addGraph( test1Url, ctxA, RDFFormat.TURTLE, mc );
            assertEquals( countStatements( mc, ctxA ), 2 );
            addGraph( test2Url, ctxA, RDFFormat.TURTLE, mc );
            assertEquals( countStatements( mc, ctxA ), 4 );

            addGraph( test1Url, ctxB, RDFFormat.TURTLE, mc );
            assertEquals( countStatements( mc, ctxB ), 2 );
            addGraph( test2Url, ctxB, RDFFormat.TURTLE, mc );
            assertEquals( countStatements( mc, ctxB ), 4 );
        }

        {
            URL test1Url = RDFImporterTest.class.getResource( "rdfImporterTest1.rdf" );
            URL test2Url = RDFImporterTest.class.getResource( "rdfImporterTest2.rdf" );

            URI ctxA = createURI( "urn:org.example.test.addGraphTest.rdfxmlA#", mc );
            URI ctxB = createURI( "urn:org.example.test.addGraphTest.rdfxmlB#", mc );

            addGraph( test1Url, ctxA, RDFFormat.RDFXML, mc );
            assertEquals( countStatements( mc, ctxA ), 2 );
            addGraph( test2Url, ctxA, RDFFormat.RDFXML, mc );
            assertEquals( countStatements( mc, ctxA ), 4 );

            addGraph( test1Url, ctxB, RDFFormat.RDFXML, mc );
            assertEquals( countStatements( mc, ctxB ), 2 );
            addGraph( test2Url, ctxB, RDFFormat.RDFXML, mc );
            assertEquals( countStatements( mc, ctxB ), 4 );
        }

        mc.close();
    }
}

