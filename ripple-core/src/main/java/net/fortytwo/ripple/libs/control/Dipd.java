package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which activates the third-to-topmost item on the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Dipd extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2013_03 + "dipd",
                StackLibrary.NS_2008_08 + "dipd",
                StackLibrary.NS_2007_08 + "dipd",
                StackLibrary.NS_2007_05 + "dipd"};
    }

    public Dipd()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true),
                new Parameter("y", null, true),
                new Parameter("z", "the program to be executed", true)};
    }

    public String getComment() {
        return "x y z  =>  z! x y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleValue z, y, x;
        RippleList stack = arg;

// hack...
        z = stack.getFirst();
        stack = stack.getRest();
        y = stack.getFirst();
        stack = stack.getRest();
        x = stack.getFirst();
        stack = stack.getRest();

        solutions.put(
                stack.push(z).push(Operator.OP).push(x).push(y));
    }
}

