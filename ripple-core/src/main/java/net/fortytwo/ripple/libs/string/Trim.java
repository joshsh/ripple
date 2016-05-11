package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string, strips off all leading and trailing
 * white space, and produces the result.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Trim extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "trim",
            StringLibrary.NS_2008_08 + "trim",
            StringLibrary.NS_2007_08 + "trim"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Trim()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  s2 -- where s2 is equal to s with leading and trailing white space omitted";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object s = stack.getFirst();
        stack = stack.getRest();

        String result = mc.toString(s).trim();

        solutions.accept(
                stack.push(StringLibrary.value(result, mc, s)));
    }
}

