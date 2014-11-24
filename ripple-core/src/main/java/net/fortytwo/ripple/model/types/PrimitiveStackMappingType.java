package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleType;
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
        return instance.getRdfEquivalent();
    }

    @Override
    public StackMapping getMapping(PrimitiveStackMapping instance) {
        return null;
    }

    @Override
    public void print(PrimitiveStackMapping instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance.getRdfEquivalent());
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OTHER_RESOURCE;
    }
}
