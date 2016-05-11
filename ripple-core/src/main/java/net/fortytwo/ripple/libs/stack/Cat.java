package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive function which consumes two lists and produces the concatenation
 * of the two lists.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Cat extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "cat",
            StackLibrary.NS_2008_08 + "cat",
            StackLibrary.NS_2007_08 + "cat",
            StackLibrary.NS_2007_05 + "cat"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Cat()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l1", "a list", true),
                new Parameter("l2", "a list", true)};
    }

    public String getComment() {
        return "l1 l2  =>  l3  -- where l3 is the concatenation of Lists l1 and l2";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        Object l1, l2;

        l1 = stack.getFirst();
        stack = stack.getRest();
        l2 = stack.getFirst();
        final RippleList rest = stack.getRest();
//System.out.println("l1 = " + l1 + ", l2 = " + l2);

        final Collector<RippleList> firstLists = new Collector<RippleList>();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void accept(final RippleList list2) throws RippleException {
                Sink<RippleList> catSink = new Sink<RippleList>() {
                    public void accept(final RippleList list1) throws RippleException {
                        RippleList result = list2.concat(list1);
                        solutions.accept(
                                rest.push(result));
                    }
                };

                firstLists.writeTo(catSink);
            }
        };

        mc.toList(l1, firstLists);
        mc.toList(l2, listSink);
    }
}

