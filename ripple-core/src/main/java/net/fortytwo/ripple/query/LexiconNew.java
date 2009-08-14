package net.fortytwo.ripple.query;

import org.openrdf.model.URI;

import java.util.Iterator;

/**
 * Author: josh
 * Date: May 29, 2009
 * Time: 6:25:50 PM
 */
public interface LexiconNew
{
    // relative URIs ///////////////////////////////////////////////////////////
    
    /**
     * @return the base URI currently in scope
     */
    URI getBaseURI();

    /**
     *
     * @param uri a relative or absolute URI reference
     */
    void setBaseURI( String uri );

    // namespaces //////////////////////////////////////////////////////////////

    Iterator<String> getNamespacePrefixes();

    URI resolveNamespacePrefix( String prefix );

    void setNamespace( String prefix, URI uri );

    // keywords ////////////////////////////////////////////////////////////////

    Iterator<String> getKeywords();

    URI resolveKeyword( String keyword );
}
