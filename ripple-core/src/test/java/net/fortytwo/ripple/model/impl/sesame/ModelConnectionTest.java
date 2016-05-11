package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.IRI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ModelConnectionTest extends RippleTestCase {
    public void testCreateURI() throws Exception {
        ModelConnection mc = getTestModel().createConnection();

        IRI uri;
        String localName, namespace;

        // Hash namespaces.

        uri = createIRI("http://example.org/foo#bar", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "bar");
        assertEquals(namespace, "http://example.org/foo#");

        uri = createIRI("http://example.org/foo#", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "");
        assertEquals(namespace, "http://example.org/foo#");

        uri = createIRI("http://example.org/ns/foo/#bar", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "bar");
        assertEquals(namespace, "http://example.org/ns/foo/#");

        uri = createIRI("http://example.org/ns/foo/#", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "");
        assertEquals(namespace, "http://example.org/ns/foo/#");

        // Slash namespaces.

        uri = createIRI("http://example.org/ns/foo/bar", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "bar");
        assertEquals(namespace, "http://example.org/ns/foo/");

        uri = createIRI("http://example.org/ns/foo/", mc);
        localName = uri.getLocalName();
        namespace = uri.getNamespace();
        assertEquals(localName, "");
        assertEquals(namespace, "http://example.org/ns/foo/");

        mc.close();
    }

/*
    public void testCountStatements() throws Exception
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

    public void testAddStatements()
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
*/
}

