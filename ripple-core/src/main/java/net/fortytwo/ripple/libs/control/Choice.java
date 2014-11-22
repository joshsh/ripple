package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a Boolean value b, an item t, and an item f, then
 * produces t if b is true, otherwise f.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Choice extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            ControlLibrary.NS_2013_03 + "choice",
            LogicLibrary.NS_2008_08 + "choice",
            StackLibrary.NS_2007_08 + "choice",
            StackLibrary.NS_2007_05 + "choice"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Choice()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("b", "a boolean condition", true),
                new Parameter("t", "the value chosen if b is true", true),
                new Parameter("f", "the value chosen if b is not true", true)};
    }

    public String getComment() {
        return "b t f  =>  x  -- where x is t if b is true, otherwise f";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object f, t, b;
        RippleList stack = arg;

        f = stack.getFirst();
        stack = stack.getRest();
        t = stack.getFirst();
        stack = stack.getRest();
        b = stack.getFirst();
        stack = stack.getRest();

        Object result = mc.toBoolean(b) ? t : f;

        solutions.put(
                stack.push(result));
    }
}

