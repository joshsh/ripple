package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.types.KeyValueType;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class KeyValueMapping implements StackMapping {
    private final String key;

    public KeyValueMapping(final String key) {
        this.key = key;
    }

    public int arity() {
        return 1;
    }

    public StackMapping getInverse() throws RippleException {
        return new NullStackMapping();
    }

    public boolean isTransparent() {
        return true;
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        Object first;
        RippleList stack = arg;

        first = stack.getFirst();
        stack = stack.getRest();

        RippleType t = mc.getModel().getTypeOf(first);
        if (t instanceof KeyValueType) {
            Object value = ((KeyValueType) t).getValue(first, key, mc);
            solutions.accept(stack.push(value));
        } else {
            throw new RippleException("argument is not a key/value pair: " + first);
        }
    }
}
