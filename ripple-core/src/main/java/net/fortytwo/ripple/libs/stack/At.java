package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * Consumes a list and an index n and produces the nth item in the list, where
 * the first item in the list has an index of 1.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class At extends PrimitiveStackMapping {
    public static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "at",
            StackLibrary.NS_2008_08 + "at",
            StackLibrary.NS_2007_08 + "at",
            StackLibrary.NS_2007_05 + "at"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public At()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list", true),
                new Parameter("i", "a list index", true)};
    }

    public String getComment() {
        return "l i  =>  l[i], the member of List l at index i.  Note: lists are 1-indexed";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        final int index = mc.toNumericValue(stack.getFirst()).intValue();
        stack = stack.getRest();
        RippleValue l = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void put(RippleList list) throws RippleException {
                if (list.isNil()) {
                    throw new RippleException("list index out of bounds: " + index);
                }

                if (index < 1) {
                    throw new RippleException("list index out of bounds (note: 'at' begins counting at 1): " + index);
                }

                for (int j = 1; j < index; j++) {
                    list = list.getRest();
                    if (list.isNil()) {
                        throw new RippleException("list index out of bounds: " + index);
                    }
                }

                solutions.put(
                        rest.push(list.getFirst()));
            }
        };

        mc.toList(l, listSink);
    }
}

