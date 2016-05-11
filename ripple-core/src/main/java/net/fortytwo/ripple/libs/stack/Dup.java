package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which pushes a copy of the topmost item on the stack to the
 * head of the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Dup extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "dup",
            StackLibrary.NS_2008_08 + "dup",
            StackLibrary.NS_2007_08 + "dup",
            StackLibrary.NS_2007_05 + "dup"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Dup()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  x x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        Object x;
        RippleList stack = arg;

        x = stack.getFirst();
        stack = stack.getRest();

        solutions.accept(
                stack.push(x).push(x));
    }
}

