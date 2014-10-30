package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes an item and a list, prepends the item to the list,
 * then produces the resulting list.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Drop extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "drop",
            StackLibrary.NS_2008_08 + "drop"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Drop() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list", true),
                new Parameter("n", "a non-negative number", true)};
    }

    public String getComment() {
        return "l n  =>  l2, the result of deleting the first n elements of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        // Note: a bad numeric value will cause an error.  However, an
        // out-of-range numeric value (e.g. -1 or a value which exceeds the
        // length of the list) will simply fail to produce a result. 
        final int n = mc.toNumericValue(stack.getFirst()).intValue();
        if (0 > n) {
            return;
        }

        stack = stack.getRest();
        final RippleValue l = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void put(final RippleList list) throws RippleException {
                RippleList result = drop(list, n);
                if (null != result) {
                    solutions.put(
                            rest.push(result));
                }
            }
        };

        mc.toList(l, listSink);
    }

    private RippleList drop(final RippleList list,
                            final int n) {
        RippleList cur = list;

        for (int i = 0; i < n; i++) {
            if (cur.isNil()) {
                return null;
            }

            cur = cur.getRest();
        }

        return cur;
    }
}
