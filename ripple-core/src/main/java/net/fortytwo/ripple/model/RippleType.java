package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Value;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RippleType<T> {
    // TODO: rpl:op may not be handled consistently.
    enum Category {
        KEYVALUE_VALUE,
        LIST,
        NUMERIC_LITERAL,
        OPERATOR,
        OTHER_RESOURCE,  // Note: includes PrimitiveStackMapping
        OTHER_LITERAL,
        STRING_LITERAL_WITHOUT_LANGUAGE_TAG,
        STRING_LITERAL_WITH_LANGUAGE_TAG,
        STRING_TYPED_LITERAL,
    }

    Collection<Class> getInstanceClasses();

    boolean isInstance(T instance);

    Value toRDF(T instance, ModelConnection mc) throws RippleException;

    StackMapping getMapping(T instance);

    void print(T instance, RipplePrintStream p, ModelConnection mc) throws RippleException;

    Category getCategory();

    int compare(T o1, T o2, ModelConnection mc);
}
