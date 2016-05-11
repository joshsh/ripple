package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a list and produces the greatest item in the list.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Max extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "max",
            StackLibrary.NS_2008_08 + "max",
            StackLibrary.NS_2007_08 + "max"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Max()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list", true)};
    }

    public String getComment() {
        return "l  =>  x   -- where x is the greatest member of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object l;

        l = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void accept(RippleList list) throws RippleException {
                Object result = null;
                while (!list.isNil()) {
                    Object v = list.getFirst();

                    if (null == result || mc.getComparator().compare(v, result) > 0) {
                        result = v;
                    }

                    list = list.getRest();
                }

                if (null != result) {
                    solutions.accept(
                            rest.push(result));
                }
            }
        };

        mc.toList(l, listSink);
    }
}

