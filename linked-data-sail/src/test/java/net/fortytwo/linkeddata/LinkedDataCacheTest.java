package net.fortytwo.linkeddata;

import org.junit.Test;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;

import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataCacheTest {
    @Test
    public void testMediaTypes() throws Exception {
        Sail sail = new MemoryStore();
        sail.initialize();

        LinkedDataCache cache = LinkedDataCache.createDefault(sail);
        String header = cache.getAcceptHeader();
        assertTrue(header.contains("application/rdf+xml"));
        assertTrue(header.contains("text/plain;q=0.5"));
        assertTrue(header.contains("application/xml;q=0.5"));
        assertTrue(header.contains("application/x-trig;q=0.5"));
        assertTrue(header.contains("application/x-turtle;q=0.5"));
        assertTrue(header.contains("text/n3;q=0.5"));
        assertTrue(header.contains("application/rdf+json;q=0.5"));
        assertTrue(header.contains("application/ld+json;q=0.5"));
        //assertTrue(header.contains("application/xhtml+xml;q=0.5"));
        //assertTrue(header.contains("application/html;q=0.5"));
        //assertTrue(header.contains("text/html;q=0.5"));
        assertTrue(header.contains("text/turtle;q=0.5"));
        assertTrue(header.contains("text/x-nquads;q=0.5"));
        assertTrue(header.contains("text/rdf+n3;q=0.5"));
        assertTrue(header.contains("application/trix;q=0.5"));
        assertTrue(header.contains("application/x-binary-rdf;q=0.5"));
        assertTrue(header.contains("image/jpeg;q=0.4"));
        assertTrue(header.contains("image/tiff;q=0.4"));
        assertTrue(header.contains("image/tiff-fx;q=0.4"));
    }
}
