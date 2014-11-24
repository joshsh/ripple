package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.RippleType;
import org.openrdf.model.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URIType extends RDFValueType<URI> {
    public URIType() {
        super(URI.class);
    }

    @Override
    public boolean isInstance(URI instance) {
        return true;
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OTHER_RESOURCE;
    }
}