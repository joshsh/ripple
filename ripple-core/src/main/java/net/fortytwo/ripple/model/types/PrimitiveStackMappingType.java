package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PrimitiveStackMappingType extends SimpleType<PrimitiveStackMapping> {

    public PrimitiveStackMappingType(final Class<? extends PrimitiveStackMapping> clazz) {
        super(clazz);
    }

    @Override
    public boolean isInstance(PrimitiveStackMapping instance) {
        // impose no additional constraints; the instance is already known to be a member of this type's
        // specific PrimitiveStackMapping subclass
        return true;
    }

    @Override
    public Value toRDF(PrimitiveStackMapping instance, ModelConnection mc) throws RippleException {
        return instance.getRDFEquivalent();
    }

    @Override
    public StackMapping getMapping(PrimitiveStackMapping instance) {
        return null;
    }

    @Override
    public void print(PrimitiveStackMapping instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance.getRDFEquivalent());
    }

    @Override
    public Category getCategory() {
        return Category.OPERATOR;
    }

    @Override
    public int compare(PrimitiveStackMapping o1, PrimitiveStackMapping o2, ModelConnection mc) {
        Value o1r = o1.getRDFEquivalent(), o2r = o2.getRDFEquivalent();
        return null == o1r
                ? null == o2r ? 0 : -1
                : null == o2r ? 1 : o1r.stringValue().compareTo(o2r.stringValue());
    }
}
