package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string and produces its (RFC 3986)
 * percent-encoded equivalent.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PercentEncoded extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "percent-encoded",
            StringLibrary.NS_2008_08 + "percentEncode",
            StringLibrary.NS_2007_08 + "percentEncode",
            SystemLibrary.NS_2007_05 + "urlEncoding"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public PercentEncoded()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("plaintext", null, true)};
    }

    public String getComment() {
        return "finds the percent encoding (per RFC 3986) of a string";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        RippleValue a = stack.getFirst();
        stack = stack.getRest();

        String result = StringUtils.percentEncode(mc.toString(a));
        solutions.put(
                stack.push(StringLibrary.value(result, mc, a)));
    }
}

