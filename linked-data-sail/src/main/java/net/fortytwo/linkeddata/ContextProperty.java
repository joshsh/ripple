package net.fortytwo.linkeddata;

import org.openrdf.model.URI;

/**
 * A key-value pair representing a property of a named graph context
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ContextProperty {
    public String key;
    public String value;  // must not break parseability of the "compact" memo string
    public URI valueDatatype;  // may be null
}
