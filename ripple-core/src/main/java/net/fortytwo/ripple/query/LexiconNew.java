package net.fortytwo.ripple.query;

import org.openrdf.model.IRI;

import java.util.Iterator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface LexiconNew {

    /**
     * @return the base URI currently in scope
     */
    IRI getBaseURI();

    /**
     * @param uri a relative or absolute URI reference
     */
    void setBaseURI(String uri);

    Iterator<String> getNamespacePrefixes();

    IRI resolveNamespacePrefix(String prefix);

    void setNamespace(String prefix, IRI uri);

    Iterator<String> getKeywords();

    IRI resolveKeyword(String keyword);
}
