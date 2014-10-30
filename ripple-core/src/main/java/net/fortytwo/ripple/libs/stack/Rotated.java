package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which permutes the second, third and fourth items on the stack
 * such that (... x y z ...) becomes (... z y x ...).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Rotated extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "rotated",
            StackLibrary.NS_2008_08 + "rotated",
            StackLibrary.NS_2007_08 + "rotated",
            StackLibrary.NS_2007_05 + "rotated"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Rotated()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true),
                new Parameter("z", null, true),
                new Parameter("a", null, true)};
    }

    public String getComment() {
        return "x y z a  =>  z y x a";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        RippleValue w, z, y, x;

        w = stack.getFirst();
        stack = stack.getRest();
        z = stack.getFirst();
        stack = stack.getRest();
        y = stack.getFirst();
        stack = stack.getRest();
        x = stack.getFirst();
        stack = stack.getRest();

        solutions.put(
                stack.push(z).push(y).push(x).push(w));
    }
}

