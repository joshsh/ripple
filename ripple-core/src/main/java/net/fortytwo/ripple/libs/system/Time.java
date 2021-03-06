package net.fortytwo.ripple.libs.system;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.lang.System;

/**
 * A primitive which produces the current time, in seconds since midnight UTC of
 * January 1, 1970.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Time extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            SystemLibrary.NS_2013_03 + "time",
            SystemLibrary.NS_2008_08 + "time",
            SystemLibrary.NS_2007_08 + "time",
            SystemLibrary.NS_2007_05 + "time"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters() {
        return new Parameter[]{};
    }

    public String getComment() {
        return "produces the current time in milliseconds since the Unix epoch";
    }

    public Time() {
        super();
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        solutions.accept(arg.push(
                System.currentTimeMillis()));
    }
}
