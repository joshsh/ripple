package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which activates the second-to-topmost item on the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Dip extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2013_03 + "dip",
                StackLibrary.NS_2008_08 + "dip",
                StackLibrary.NS_2007_08 + "dip",
                StackLibrary.NS_2007_05 + "dip"};
    }

    public Dip()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", "placed above the executed program y on the stack", true),
                new Parameter("y", "the program to be executed", true)};
    }

    public String getComment() {
        return "x y  =>  y! x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        Object y, x;
        RippleList stack = arg;

// hack...
        y = stack.getFirst();
        stack = stack.getRest();
        x = stack.getFirst();
        stack = stack.getRest();

        solutions.accept(
                stack.push(y).push(Operator.OP).push(x));
    }
}

