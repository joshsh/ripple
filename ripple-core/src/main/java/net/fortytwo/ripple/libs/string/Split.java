package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A primitive which consumes a string and a regular expression, then produces
 * the list obtained by "splitting" the string around the regular expression.
 * For instance <code>... "one, two,three" ",[ ]*" /split</code> yields
 * <code>... ("one" "two" "three")</code>
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Split extends PrimitiveStackMapping {
    private static final Logger logger = Logger.getLogger(Split.class.getName());

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
        return "s regex  =>  (s1, s2, s3, ...) -- where s has been divided into substrings" +
                " by occurrences of regular expression regex";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object s, regex;

        regex = stack.getFirst();
        stack = stack.getRest();
        s = stack.getFirst();
        stack = stack.getRest();

        try {
            String regexStr = mc.toString(regex);
            if (0 == regexStr.length()) {
                // splitting on an empty string is undefined in Ripple.
                // JDK 1.7 and 1.8 appear to handle this case differently.
                // Simply produce no solutions.
                return;
            }

            String[] array = mc.toString(s).split(regexStr);
            RippleList result = mc.list();
            for (int i = array.length - 1; i >= 0; i--) {
                result = result.push(StringLibrary.value(array[i], mc, s, regex));
            }

            try {
                solutions.accept(
                        stack.push(result));
            } catch (RippleException e) {
                // Soft fail
                logger.log(Level.WARNING, "failed to put solution", e);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            // Hard fail (for now).
            throw new RippleException(e);
        }
    }
}

