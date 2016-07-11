package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and two integer indexes, then
 * produces the substring between the first index (inclusive) and the second
 * index (exclusive).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Substring extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "substring",
            StringLibrary.NS_2008_08 + "substring",
            StringLibrary.NS_2007_08 + "substring"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Substring() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true),
                new Parameter("beginIndex", null, true),
                new Parameter("endIndex", null, true)};
    }

    public String getComment() {
        return "s beginIndex endIndex  =>  s2 -- where s2 is the substring of s which begins" +
                " at the specified beginIndex and extends to the character at index endIndex - 1";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        int begin, end;
        Object s;
        String result;

        end = mc.toNumber(stack.getFirst()).intValue();
        stack = stack.getRest();
        begin = mc.toNumber(stack.getFirst()).intValue();
        stack = stack.getRest();
        s = stack.getFirst();
        stack = stack.getRest();

        try {
            result = mc.toString(s).substring(begin, end);
            solutions.accept(
                    stack.push(StringLibrary.value(result, mc, s)));
        } catch (IndexOutOfBoundsException e) {
            // Silent fail.
        }
    }
}

