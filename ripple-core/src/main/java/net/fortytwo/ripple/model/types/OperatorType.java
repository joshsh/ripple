package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class OperatorType extends SimpleType<Operator> {

    public OperatorType() {
        super(Operator.class);
    }

    @Override
    public boolean isInstance(Operator instance) {
        return true;
    }

    @Override
    public Value toRDF(Operator instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(Operator instance) {
        return instance.getMapping();
    }

    @Override
    public void print(Operator instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("[Operator: ");
        p.print(getMapping(instance));
        p.print("]");
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OPERATOR;
    }

    @Override
    public int compare(Operator o1, Operator o2, ModelConnection mc) {
        // note: this is an impermanent and inconsistent ordering
        return ((Integer) o1.getMapping().hashCode()).compareTo(o2.getMapping().hashCode());
    }
}
