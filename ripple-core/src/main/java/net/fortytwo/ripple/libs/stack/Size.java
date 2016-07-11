package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a list and produces the length of the list as
 * an integer.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Size extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "size",
            StackLibrary.NS_2008_08 + "size",
            StackLibrary.NS_2007_08 + "size",
            StackLibrary.NS_2007_05 + "size"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Size() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list", true)};
    }

    public String getComment() {
        return "l  =>  n   -- where n is the number of members of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object l;

        l = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<RippleList> listSink = list -> {
            int result = list.length();
            solutions.accept(
                    rest.push(result));
        };

        mc.toList(l, listSink);
    }
}

