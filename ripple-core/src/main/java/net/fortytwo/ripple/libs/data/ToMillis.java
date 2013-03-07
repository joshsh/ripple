package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.util.Date;

/**
 * A primitive which produces the current time, in seconds since midnight UTC of
 * January 1, 1970.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToMillis extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2013_03 + "to-millis",
                SystemLibrary.NS_2008_08 + "dateTimeToMillis",
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("dateTime", null, true)};
    }

    public String getComment() {
        return "converts an xsd:dateTime value to milliseconds since the Unix epoch";
    }

    public ToMillis() throws RippleException {
        super();
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        Date d = mc.toDateValue(stack.getFirst());
        stack = stack.getRest();

        solutions.put(stack.push(
                mc.numericValue(d.getTime())));
    }
}
