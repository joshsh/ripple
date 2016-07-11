package net.fortytwo.ripple.libs.logic;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes two Boolean values and produces the result of
 * their inclusive logical disjunction.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Or extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2013_03 + "or",
            LogicLibrary.NS_2008_08 + "or",
            StackLibrary.NS_2007_08 + "or",
            StackLibrary.NS_2007_05 + "or"};

    private final StackMapping self = this;

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Or() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", "a boolean value (xsd:true or xsd:false)", true),
                new Parameter("y", "a boolean value (xsd:true or xsd:false)", true)};
    }

    public String getComment() {
        return "x y  =>  z  -- where z is true if x or y is true, otherwise false";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        boolean x, y;

        x = mc.toBoolean(stack.getFirst());
        stack = stack.getRest();
        y = mc.toBoolean(stack.getFirst());
        stack = stack.getRest();

        boolean result = x || y;

        solutions.accept(
                stack.push(result));
    }

    @Override
    public StackMapping getInverse() {
        return new StackMapping() {
            public int arity() {
                return 1;
            }

            public StackMapping getInverse() throws RippleException {
                return self;
            }

            public boolean isTransparent() {
                return true;
            }

            public void apply(final RippleList arg,
                              final Sink<RippleList> solutions,
                              final ModelConnection mc) throws RippleException {

                RippleList stack = arg;

                boolean x;

                x = mc.toBoolean(stack.getFirst());
                stack = stack.getRest();

                if (x) {
                    Object t = true;
                    Object f = false;
                    solutions.accept(stack.push(t).push(t));
                    solutions.accept(stack.push(t).push(f));
                    solutions.accept(stack.push(f).push(t));
                } else {
                    Object f = false;
                    solutions.accept(stack.push(f).push(f));
                }
            }
        };
    }
}

