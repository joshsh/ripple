package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StackMappingWrapper;
import org.openrdf.model.Value;

/**
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
}
