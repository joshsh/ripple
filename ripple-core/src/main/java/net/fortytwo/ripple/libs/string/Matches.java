package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and a regular expression, producing a
 * Boolean value of true if the regular expression matches the string, otherwise
 * false.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Matches extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "matches",
            StringLibrary.NS_2008_08 + "matches",
            StringLibrary.NS_2007_08 + "matches"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Matches() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true),
                new Parameter("regex", null, true)};
    }

    public String getComment() {
        return "s regex  =>  b -- where b is true if the given string matches the given regular expression," +
                " otherwise false";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        String regex, s;
        boolean result;

        regex = mc.toString(stack.getFirst());
        stack = stack.getRest();
        s = mc.toString(stack.getFirst());
        stack = stack.getRest();

        try {
            result = s.matches(regex);

            solutions.accept(
                    stack.push(result));
        } catch (java.util.regex.PatternSyntaxException e) {
            // Hard fail (for now).
            throw new RippleException(e);
        }
    }
}

