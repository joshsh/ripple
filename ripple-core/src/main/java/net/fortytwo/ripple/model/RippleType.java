package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Value;

import java.util.Collection;
import java.util.Comparator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RippleType<T> {
    // TODO: rpl:op may not be handled consistently.
    public enum Category {
        KEYVALUE_VALUE,
        LIST,
        NUMERIC_TYPED_LITERAL,
        OPERATOR,
        OTHER_RESOURCE,  // Note: includes PrimitiveStackMapping
        OTHER_TYPED_LITERAL,
        PLAIN_LITERAL_WITHOUT_LANGUAGE_TAG,
        PLAIN_LITERAL_WITH_LANGUAGE_TAG,
    }

    Collection<Class> getInstanceClasses();

    boolean isInstance(T instance);

    Value toRDF(T instance, ModelConnection mc) throws RippleException;

    StackMapping getMapping(T instance);

    void print(T instance, RipplePrintStream p, ModelConnection mc) throws RippleException;

    Category getCategory();

    int compare(T o1, T o2, ModelConnection mc);
}
