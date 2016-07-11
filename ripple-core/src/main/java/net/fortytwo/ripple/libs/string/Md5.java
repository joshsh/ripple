package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string and produces its SHA-1 sum.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Md5 extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "md5",
            StringLibrary.NS_2008_08 + "md5"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Md5() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("plaintext", null, true)};
    }

    public String getComment() {
        return "finds the md5 hash of a string";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object a = stack.getFirst();
        stack = stack.getRest();

        String result = StringUtils.md5SumOf(mc.toString(a));
        solutions.accept(stack.push(
                StringLibrary.value(result, mc, a)));
    }
}
