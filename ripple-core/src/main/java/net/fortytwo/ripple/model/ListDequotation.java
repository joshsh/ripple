package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;


/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ListDequotation implements StackMapping {
    private final RippleList list;
    private boolean invert = false;

    public ListDequotation(final RippleList list) {
        this.list = list;
    }

    public int arity() {
        return 0;
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList in = list;
        RippleList out = arg;

        while (!in.isNil()) {
            out = out.push(in.getFirst());
            in = in.getRest();
        }

        // Never emit an empty stack.
        if (!out.isNil()) {
            if (invert) {
                Object v = out.getFirst();
                StackMapping m = mc.toMapping(v);
                if (null != m) {
                    StackMapping inverse = m.getInverse();
                    out = out.getRest().push(new Operator(inverse));
                    solutions.accept(out);
                }
            } else {
                solutions.accept(out);
            }
        }
    }

    public boolean isTransparent() {
        return true;
    }

    public StackMapping getInverse() throws RippleException {
        ListDequotation m = new ListDequotation(list);
        m.invert = true;
        return m;
    }

    public String toString() {
        return "Dequote(" + list + ")";
    }
}
