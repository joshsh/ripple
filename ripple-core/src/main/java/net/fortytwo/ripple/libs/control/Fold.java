package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a list, an "initial value" and a filter, then
 * produces the result of folding the list with the given filter and initial
 * value.  For instance, <code>(1 2 3) 0 add /fold</code> yields 0 + 1 + 2 + 3
 * = 6.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Fold extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2013_03 + "fold",
                StackLibrary.NS_2008_08 + "fold",
                StackLibrary.NS_2007_08 + "fold",
                StackLibrary.NS_2007_05 + "fold"};
    }

    public Fold()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list through which to fold p", true),
                new Parameter("v0", "initial value", true),
                new Parameter("p", "a binary function", true)};
    }

    public String getComment() {
        return "l v0 p  =>  v  -- starting with value v0," +
                " sequentially push members of List l and combine with binary operator p";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        Object l;

        final Object f = stack.getFirst();
        stack = stack.getRest();
        final Object v = stack.getFirst();
        stack = stack.getRest();
        l = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void put(final RippleList list) throws RippleException {
                //RippleList lList = list.invert();
                RippleList lList = list;
                RippleList result = rest.push(v);

                while (!lList.isNil()) {
                    result = result.push(lList.getFirst())
                            .push(f)
                            .push(Operator.OP);
                    lList = lList.getRest();
                }

                solutions.put(result);
            }
        };

        mc.toList(l, listSink);
    }
}

