package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

/**
 * The data type of a stack mapping (without a wrapper or RDF equivalent)
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StackMappingType extends SimpleType<StackMapping> {

    public StackMappingType() {
        super(StackMapping.class);
    }

    @Override
    public boolean isInstance(StackMapping instance) {
        return true;
    }

    @Override
    public Value toRDF(StackMapping instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(StackMapping instance) {
        return instance;
    }

    @Override
    public void print(StackMapping instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("[" + instance.getClass().getName() + "]");
    }

    @Override
    public Category getCategory() {
        return Category.OPERATOR;
    }

    @Override
    public int compare(StackMapping o1, StackMapping o2, ModelConnection mc) {
        // note: this is an impermanent and inconsistent ordering
        return ((Integer) o1.hashCode()).compareTo(o2.hashCode());
    }
}