package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BooleanType extends SimpleType<Boolean> {

    public BooleanType() {
        super(Boolean.class);
    }

    @Override
    public boolean isInstance(Boolean instance) {
        return true;
    }

    @Override
    public Value toRDF(Boolean instance, ModelConnection mc) throws RippleException {
        return ((SesameModelConnection) mc).getValueFactory().createLiteral(instance);
    }

    @Override
    public StackMapping getMapping(Boolean instance) {
        return null;
    }

    @Override
    public void print(Boolean instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance);
    }

    @Override
    public Category getCategory() {
        return Category.OTHER_TYPED_LITERAL;
    }

    @Override
    public int compare(Boolean o1, Boolean o2, ModelConnection mc) {
        return o1.compareTo(o2);
    }
}
