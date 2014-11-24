package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string, maps its characters to upper case, and
 * produces the result.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToUpperCase extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "to-upper-case",
            StringLibrary.NS_2008_08 + "toUpperCase",
            StringLibrary.NS_2007_08 + "toUpperCase"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public ToUpperCase()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  s2 -- where s2 is equal to s with all characters converted to upper case";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object s = stack.getFirst();
        stack = stack.getRest();

        String result = mc.toString(s).toUpperCase();

        solutions.put(
                stack.push(StringLibrary.value(result, mc, s)));
    }
}

