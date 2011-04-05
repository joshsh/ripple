package net.fortytwo.ripple.model.keyval;

import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;

/**
 * User: josh
 * Date: Aug 18, 2010
 * Time: 1:42:37 PM
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

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions) throws RippleException {
        RippleValue x;
        RippleList stack = arg.getStack();

        x = stack.getFirst();
        stack = stack.getRest();

        if (x instanceof KeyValueValue) {
            RippleValue result = ((KeyValueValue) x).getValue(key, arg.getModelConnection());
            solutions.put(arg.with(
                    stack.push(result)));
        } else {
            throw new RippleException("argument is not a JSON value: " + x);
        }
    }
}
