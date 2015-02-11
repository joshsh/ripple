package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes an  (RFC 3986) percent-encoded string and produces
 * its decoded equivalent.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PercentDecoded extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "percent-decoded",
            StringLibrary.NS_2008_08 + "percentDecode"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public PercentDecoded()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("encoded", null, true)};
    }

    public String getComment() {
        return "decodes a (RFC 3986) percent-encoded string";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object a = stack.getFirst();
        stack = stack.getRest();

        String result = StringUtils.percentDecode(mc.toString(a));
        solutions.put(
                stack.push(StringLibrary.value(result, mc, a)));
    }
}

