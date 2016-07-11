package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NumericLiteralType extends NumericType<Literal> {

    private final Collection<Class> classes = Arrays.asList(new Class[]{Literal.class});

    @Override
    public Collection<Class> getInstanceClasses() {
        return classes;
    }

    @Override
    public boolean isInstance(Literal instance) {
        return null != findDatatype(instance);
    }

    @Override
    public Value toRDF(Literal instance, ModelConnection mc) throws RippleException {
        Datatype datatype = findDatatype(instance);
        Number number = findNumber(instance, datatype);

        return toRDF(number, datatype, mc);
    }

    @Override
    public void print(Literal instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        Datatype datatype = findDatatype(instance);
        Number number = findNumber(instance, datatype);

        printTo(number, findDatatype(instance), p);
    }

    @Override
    public Datatype findDatatype(final Literal instance) {
        return dataTypeOf(instance);
    }

    @Override
    public Number findNumber(final Literal instance, final Datatype datatype) {

        if (null == datatype) {
            throw new IllegalArgumentException("numeric value is of unknown class: " + instance.getClass());
        }

        switch (datatype) {
            case DECIMAL:
                return instance.decimalValue();
            case DOUBLE:
                return doubleValue(instance);
            case FLOAT:
                return instance.floatValue();
            case INTEGER:
                return instance.intValue();
            case LONG:
                return instance.longValue();
            default:
                throw new IllegalStateException();
        }
    }
}
