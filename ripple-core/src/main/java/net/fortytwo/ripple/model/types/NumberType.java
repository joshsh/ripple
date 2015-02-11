package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import org.openrdf.model.Value;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NumberType extends NumericType<Number> {

    @Override
    public Collection<Class> getInstanceClasses() {
        return datatypeByClass.keySet();
    }

    @Override
    public boolean isInstance(final Number instance) {
        return null != findDatatype(instance);
    }

    @Override
    public void print(final Number instance,
                      final RipplePrintStream p,
                      final ModelConnection mc) throws RippleException {
        Datatype datatype = findDatatype(instance);

        if (null == datatype) {
            throw new IllegalArgumentException("numeric value is of unknown class: " + instance.getClass());
        }

        printTo(instance, datatype, p);
    }

    @Override
    public Value toRDF(Number instance, ModelConnection mc) throws RippleException {
        Datatype datatype = findDatatype(instance);
        if (null == datatype) {
            throw new IllegalArgumentException();
        }

        return toRDF(instance, datatype, mc);
    }

    @Override
    public Datatype findDatatype(final Number instance) {
        return datatypeByClass.get(instance.getClass());
    }

    @Override
    public Number findNumber(Number instance, Datatype datatype) {
        return instance;
    }
}
