package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which removes the topmost item from the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Pop extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "pop",
            StackLibrary.NS_2008_08 + "pop",
            StackLibrary.NS_2007_08 + "pop",
            StackLibrary.NS_2007_05 + "pop"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Pop() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        stack = stack.getRest();

        if (!stack.isNil()) {
            solutions.accept(stack);
        }
    }
}

