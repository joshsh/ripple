package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a Boolean filter b, a filter t, and a filter t,
 * then applies an active copy of b to the stack.  If b yields a value of
 * true, then t is applied the rest of the stack.  Otherwise, f is applied to
 * the rest of the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class While extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            ControlLibrary.NS_2013_03 + "while",
            LogicLibrary.NS_2008_08 + "while"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public While() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("b", "loop condition", true),
                new Parameter("p", "loop body, executed as long as condition is satisfied", true)};
    }

    public String getComment() {
        return "b p =>  p! p! p! ...  -- where p is executed as long as executing b yields true";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        Object program = stack.getFirst();
        stack = stack.getRest();
        Object criterion = stack.getFirst();
        stack = stack.getRest();

        Collector<Operator> programOps
                = new Collector<Operator>();
        Operator.createOperator(program, programOps, mc);
        Collector<Operator> criterionOps
                = new Collector<Operator>();
        Operator.createOperator(criterion, criterionOps, mc);

        for (Operator programOp : programOps) {
            for (Operator criterionOp : criterionOps) {
                StackMapping a = new WhileApplicator(programOp, criterionOp);

                solutions.put(stack.push(new Operator(a)));
            }
        }
    }

    private class WhileDecider implements StackMapping {
        private final RippleList originalStack;
        private final Operator program;
        private final Operator criterion;

        public WhileDecider(final RippleList originalStack,
                            final Operator program,
                            final Operator criterion) {
            this.originalStack = originalStack;
            this.program = program;
            this.criterion = criterion;
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

            boolean b = mc.toBoolean(arg.getFirst());

            if (b) {
                StackMapping a = new WhileApplicator(program, criterion);
                RippleList stack = originalStack.push(program).push(new Operator(a));
                solutions.put(stack);
            } else {
                solutions.put(originalStack);
            }
        }
    }

    private class WhileApplicator implements StackMapping {
        private final Operator program;
        private final Operator criterion;

        public WhileApplicator(final Operator program,
                               final Operator criterion) {
            this.program = program;
            this.criterion = criterion;
        }

        public int arity() {
            // Cheat which forces the program below to be applied.
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
            StackMapping d = new WhileDecider(stack, program, criterion);

            solutions.put(stack.push(criterion).push(new Operator(d)));
        }
    }
}
