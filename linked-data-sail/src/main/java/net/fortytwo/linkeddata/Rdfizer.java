package net.fortytwo.linkeddata;

import org.openrdf.model.URI;
import org.openrdf.rio.RDFHandler;

import java.io.InputStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Rdfizer {
    /**
     * @param is          a stream representation of the resource to be converted
     * @param handler
     * @param resourceUri
     * @param baseUri
     * @return
     */
    ContextMemo.Status rdfize(InputStream is, RDFHandler handler, URI resourceUri, String baseUri);
}
