package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.RippleType;
import org.openrdf.model.BNode;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BNodeType extends RDFValueType<BNode> {
    public BNodeType() {
        super(BNode.class);
    }

    @Override
    public boolean isInstance(BNode instance) {
        return true;
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OTHER_RESOURCE;
    }
}
