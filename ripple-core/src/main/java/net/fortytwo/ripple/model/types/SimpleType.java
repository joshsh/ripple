package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.RippleType;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class SimpleType<T> implements RippleType<T> {
    private final Collection<Class> classes;

    @Override
    public Collection<Class> getInstanceClasses() {
        return classes;
    }

    protected SimpleType(Class... classes) {
        this.classes = Arrays.asList(classes);
    }
}
