package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import org.openrdf.model.IRI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class IRIType extends RDFValueType<IRI> {
    public IRIType() {
        super(IRI.class);
    }

    @Override
    public boolean isInstance(IRI instance) {
        return true;
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OTHER_RESOURCE;
    }

    @Override
    public int compare(IRI o1, IRI o2, ModelConnection mc) {
        return o1.stringValue().compareTo(o2.stringValue());
    }
}