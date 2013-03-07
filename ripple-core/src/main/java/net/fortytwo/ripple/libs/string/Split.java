package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string and a regular expression, then produces
 * the list obtained by "splitting" the string around the regular expression.
 * For instance <code>... "one, two,three" ",[ ]*" /split</code> yields
 * <code>... ("one" "two" "three")</code>
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Split extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "split",
            StringLibrary.NS_2008_08 + "split",
            StringLibrary.NS_2007_08 + "split"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Split()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true),
                new Parameter("regex", null, true)};
    }

    public String getComment() {
        return "s regex  =>  (s1, s2, s3, ...) -- where s has been divided into substrings by occurrences of regular expression regex";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        RippleValue s, regex;

        regex = stack.getFirst();
        stack = stack.getRest();
        s = stack.getFirst();
        stack = stack.getRest();

        try {
            String[] array = mc.toString(s).split(mc.toString(regex));
            RippleList result = mc.list();
            for (int i = array.length - 1; i >= 0; i--) {
                result = result.push(StringLibrary.value(array[i], mc, s, regex));
            }

            try {
                solutions.put(
                        stack.push(result));
            } catch (RippleException e) {
                // Soft fail
                e.logError();
            }
        }

        catch (java.util.regex.PatternSyntaxException e) {
            // Hard fail (for now).
            throw new RippleException(e);
        }
    }
}

