package net.fortytwo.linkeddata;

import org.openrdf.rio.RDFHandler;

import java.io.InputStream;

/**
 * An object which consumes a document and extracts RDF statements from it,
 * creating an RDF document.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Rdfizer {
    /**
     * @param is      a stream representation of the resource to be consumed
     * @param handler a handler for the extracted RDF statements
     * @param baseUri the base URI of the extracted RDF document
     * @return the outcome of the rdfize operation
     */
    CacheEntry.Status rdfize(InputStream is, RDFHandler handler, String baseUri);
}
