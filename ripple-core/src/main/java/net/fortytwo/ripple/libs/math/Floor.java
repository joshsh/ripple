package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a number and produces the largest integer value
 * which is less than or equal to the number.  Note: the produced value has
 * a type of xsd:integer, unlike Java's Math.floor, which returns a double
 * value.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Floor extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "floor",
            MathLibrary.NS_2008_08 + "floor"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Floor()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  f, where f is the largest integer value that is less than or equal to x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = (int) Math.floor(a.doubleValue());

        solutions.put(
                stack.push(result));
    }
}

