package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a list and a mapping, then applies the mapping to
 * each element in the list (as if it were on top of the remainder of the stack)
 * in turn, yielding another list.
 * For instance, <code>(1 2 3) (10 add.) map.</code> yields <code>(11 12 13)</code>
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Map extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2013_03 + "map",
                StackLibrary.NS_2008_08 + "map"};
    }

    public Map() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a List to map through m", true),
                new Parameter("m", "a mapping which produces and consumes one argument", true)};
    }

    public String getComment() {
        return "l m  =>  l mapped through m";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        final Object mappingVal = stack.getFirst();
        stack = stack.getRest();
        Object listVal = stack.getFirst();
        final RippleList rest = stack.getRest();

        // Note: it is simply assumed that these mappings have a production of
        // exactly one item.
        final Collector<Operator> operators = new Collector<>();
        Operator.createOperator(mappingVal, operators, mc);

        Sink<RippleList> listSink = list -> {
            if (list.isNil()) {
                solutions.accept(rest.push(list));
            }

            // TODO: this is probably a little more complicated than it needs to be
            else {
                RippleList inverted = list.invert();
                Object f = inverted.getFirst();

                for (Operator operator : operators) {
                    StackMapping inner = new InnerMapping(mc.list(), inverted.getRest(), operator);
                    solutions.accept(rest.push(f).push(mappingVal).push(Operator.OP).push(new Operator(inner)));
                }
            }
        };

        mc.toList(listVal, listSink);
    }

    private class InnerMapping implements StackMapping {
        private final RippleList invertedListHead;
        private final RippleList constructedList;
        private final Operator operator;

        public InnerMapping(final RippleList constructedList,
                            final RippleList invertedListHead,
                            final Operator operator) {
            this.constructedList = constructedList;
            this.invertedListHead = invertedListHead;
            this.operator = operator;
        }

        public int arity() {
            return 1;
        }

        public StackMapping getInverse() throws RippleException {
            return new NullStackMapping();
        }

        public boolean isTransparent() {
            return true;
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {

            RippleList stack = arg;
            Object first = stack.getFirst();
            stack = stack.getRest();

            RippleList newListRest = constructedList.push(first);

            if (invertedListHead.isNil()) {
                solutions.accept(stack.push(newListRest));
            } else {
                // The stack to operate on
                RippleList restStack = stack.push(invertedListHead.getFirst()).push(operator);

                StackMapping restMapping = new InnerMapping(newListRest, invertedListHead.getRest(), operator);
                solutions.accept(restStack.push(new Operator(restMapping)));
            }
        }
    }
}
