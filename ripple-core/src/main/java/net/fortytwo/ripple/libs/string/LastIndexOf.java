package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and a substring and produces the index of
 * the last occurrence of the substring.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LastIndexOf extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "last-index-of",
            StringLibrary.NS_2008_08 + "lastIndexOf",
            StringLibrary.NS_2007_08 + "lastIndexOf"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public LastIndexOf()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("str", null, true),
                new Parameter("substr", null, true)};
    }

    public String getComment() {
        return "str substr  =>  i -- where i is the index of the last occurrence of substr in str," +
                " or -1 if it does not occur";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        String str, substr;
        int result;

        substr = mc.toString(stack.getFirst());
        stack = stack.getRest();
        str = mc.toString(stack.getFirst());
        stack = stack.getRest();

        result = str.lastIndexOf(substr);
        solutions.put(
                stack.push(result));
    }
}

