package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stream.StreamLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A filter which discards the stack unless the topmost item is the boolean
 * value stack:true.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Require extends PrimitiveStackMapping {
    @Override
    public int arity() {
        return 2;
    }

    private static final String[] IDENTIFIERS = {
            ControlLibrary.NS_2013_03 + "require",
            StreamLibrary.NS_2008_08 + "require"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Require() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("f", null, true)};
    }

    public String getComment() {
        return "transmits the rest of a stack only if applying the topmost item" +
                " to the rest of the stack yields stack:true";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object mapping = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<Operator> opSink = op -> {
            CriterionApplicator applicator = new CriterionApplicator(op);
            solutions.accept(rest.push(new Operator(applicator)));
        };

        Operator.createOperator(mapping, opSink, mc);
    }

    private class CriterionApplicator implements StackMapping {
        private final Operator criterion;

        public CriterionApplicator(final Operator criterion) {
            this.criterion = criterion;
        }

        // FIXME: the criterion's arity had better be accurate
        // (which it currently may not be, if the criterion is a list dequotation)
        public int arity() {
            return criterion.getMapping().arity();
        }

        public StackMapping getInverse() throws RippleException {
            return new NullStackMapping();
        }

        public boolean isTransparent() {
            return criterion.getMapping().isTransparent();
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {

            Decider decider = new Decider(arg);

            // Apply the criterion, sending the result into the Decider.
            solutions.accept(arg.push(criterion).push(new Operator(decider)));
        }
    }

    private class Decider implements StackMapping {
        private final RippleList rest;

        public Decider(final RippleList rest) {
            this.rest = rest;
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

            Object b;
            RippleList stack = arg;

            b = stack.getFirst();

            if (mc.toBoolean(b)) {
                solutions.accept(rest);
            }
        }
    }
}
