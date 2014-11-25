package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RDFValueType<T extends Value> extends SimpleType<T> {
    protected RDFValueType(Class clazz) {
        super(clazz);
    }

    @Override
    public StackMapping getMapping(T instance) {
        return null;
    }

    @Override
    public void print(T instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance);
    }

    @Override
    public Value toRDF(T instance, ModelConnection mc) throws RippleException {
        return instance;
    }
}
