package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StackMappingWrapper;
import org.openrdf.model.Value;

/**
 * The data type of a wrapper for a stack mapping with a settable RDF equivalent
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StackMappingWrapperType extends SimpleType<StackMappingWrapper> {

    public StackMappingWrapperType() {
        super(StackMappingWrapper.class);
    }

    @Override
    public boolean isInstance(StackMappingWrapper instance) {
        return true;
    }

    @Override
    public Value toRDF(StackMappingWrapper instance, ModelConnection mc) throws RippleException {
        return instance.getRDFEquivalent();
    }

    @Override
    public StackMapping getMapping(StackMappingWrapper instance) {
        return null;
    }

    @Override
    public void print(StackMappingWrapper instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("[StackMappingWrapper: ");
        p.print(instance.getInnerMapping());
        p.print("]");
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OTHER_RESOURCE;
    }

    @Override
    public int compare(StackMappingWrapper o1, StackMappingWrapper o2, ModelConnection mc) {
        Value o1r = o1.getRDFEquivalent(), o2r = o2.getRDFEquivalent();
        return null == o1r
                ? null == o2r ? 0 : -1
                : null == o2r ? 1 : o1r.stringValue().compareTo(o2r.stringValue());
    }
}
