package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import junit.framework.TestCase;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.URIMap;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSailTest extends TestCase {
    private Sail baseSail;
    private LinkedDataSail sail;

    public void setUp() throws Exception {
        Ripple.initialize();

        baseSail = new MemoryStore();
        baseSail.initialize();

        URIMap map = new URIMap();

        // This is an example where HttpDereferencer fails by requesting the
        // full URI of a resource (rather than stripping off the local part).
        // Here, we define a mapping to a local file, so dereferencing
        // succeeds.
        map.put("http://www.holygoat.co.uk/owl/redwood/0.1/tags/Tagging",
                LinkedDataSailTest.class.getResource("tags.owl").toString());

        LinkedDataCache wc = LinkedDataCache.createDefault(baseSail);
        wc.setURIMap(map);
        sail = new LinkedDataSail(baseSail, wc);
        sail.initialize();
    }

    public void tearDown() throws Exception {
        sail.shutDown();
        baseSail.shutDown();
    }

    public void testDereferencer() throws Exception {
        long count;
        boolean includeInferred = false;
        ValueFactory vf = sail.getValueFactory();
        URI tagging = vf.createURI("http://www.holygoat.co.uk/owl/redwood/0.1/tags/Tagging");

        SailConnection sc = sail.getConnection();
        try {
            count = countStatements(sc.getStatements(tagging, RDF.TYPE, null, includeInferred));
            assertEquals(1, count);
        } finally {
            sc.close();
        }
    }

    public void testCountStatements() throws Exception {
        ValueFactory vf = sail.getValueFactory();
        SailConnection sc = sail.getConnection();

        URI ctxA = vf.createURI("urn:org.example.test.countStatementsTest#");
        URI uri1 = vf.createURI("urn:org.example.test#uri1");
        URI uri2 = vf.createURI("urn:org.example.test#uri2");
        URI uri3 = vf.createURI("urn:org.example.test#uri3");

        URI[] uris = {uri1, uri2, uri3};

        assertEquals(0, countStatements(sc, ctxA));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    sc.addStatement(
                            uris[i], uris[j], uris[k], ctxA);
                }
            }
        }
        sc.commit();
        assertEquals(27, countStatements(sc, ctxA));

        sc.close();
    }

    private static long countStatements(final CloseableIteration<? extends Statement, SailException> iter)
            throws SailException {
        long count;

        try {
            count = 0;
            while (iter.hasNext()) {
                iter.next();
                count++;
            }
        } finally {
            iter.close();
        }

        return count;
    }

    private static long countStatements(final SailConnection sc, final Resource... contexts)
            throws SailException {
        return countStatements(sc.getStatements(null, null, null, false, contexts));
    }

    // For debugging/experimentation
    public static void main(final String[] args) throws Exception {
        Ripple.initialize();

        Sail baseSail = new MemoryStore();
        baseSail.initialize();

        try {
            Repository repo = new SailRepository(baseSail);

            LinkedDataSail sail = new LinkedDataSail(baseSail);
            sail.initialize();
            try {
                SailConnection sc = sail.getConnection();
                try {
                    sc.getStatements(new URIImpl("http://rdf.freebase.com/rdf/en.stephen_fry"), null, null, false);
                    sc.commit();
                } finally {
                    sc.close();
                }
            } finally {
                sail.shutDown();
            }

            RepositoryConnection rc = repo.getConnection();
            try {
                rc.export(new RDFHandler() {
                    public void startRDF() throws RDFHandlerException {
                    }

                    public void endRDF() throws RDFHandlerException {
                    }

                    public void handleNamespace(String s, String s1) throws RDFHandlerException {
                    }

                    public void handleStatement(Statement statement) throws RDFHandlerException {
                        System.out.println("" + statement);
                    }

                    public void handleComment(String s) throws RDFHandlerException {
                    }
                });
            } finally {
                rc.close();
            }
        } finally {
            baseSail.shutDown();
        }
    }
}
