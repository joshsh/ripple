package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces its arc tangent, in the
 * range of -pi/2 through pi/2.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Atan extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "atan",
            MathLibrary.NS_2008_08 + "atan",
            MathLibrary.NS_2007_08 + "atan"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Atan()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  atan(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        Number a, result;

        a = mc.toNumber(stack.getFirst());
        stack = stack.getRest();

        result = Math.atan(a.doubleValue());

        solutions.put(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() throws RippleException {
        return MathLibrary.getTanValue();
    }
}

