package net.fortytwo.ripple.model.keyval;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;

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
        Object x;
        RippleList stack = arg;

        x = stack.getFirst();
        stack = stack.getRest();

        if (x instanceof KeyValueValue) {
            Object result = ((KeyValueValue) x).getValue(key, mc);
            solutions.put(
                    stack.push(result));
        } else {
            throw new RippleException("argument is not a JSON value: " + x);
        }
    }
}
