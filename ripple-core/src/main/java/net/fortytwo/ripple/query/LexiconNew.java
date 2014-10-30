package net.fortytwo.ripple.query;

import org.openrdf.model.URI;

import java.util.Iterator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface LexiconNew {
    // relative URIs ///////////////////////////////////////////////////////////

    /**
     * @return the base URI currently in scope
     */
    URI getBaseURI();

    /**
     * @param uri a relative or absolute URI reference
     */
    void setBaseURI(String uri);

    // namespaces //////////////////////////////////////////////////////////////

    Iterator<String> getNamespacePrefixes();

    URI resolveNamespacePrefix(String prefix);

    void setNamespace(String prefix, URI uri);

    // keywords ////////////////////////////////////////////////////////////////

    Iterator<String> getKeywords();

    URI resolveKeyword(String keyword);
}
