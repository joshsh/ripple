package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.ListMemoizer;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;


/**
 * A filter which drops any stack which has already been transmitted and behaves
 * like the identity filter otherwise, making a stream of stacks into a set of
 * stacks.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Intersect extends PrimitiveStackMapping {
    private static final String MEMO = "memo";

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2013_03 + "intersect",
            StreamLibrary.NS_2008_08 + "intersect"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Intersect() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("r1", null, true),
                new Parameter("r2", null, true)};
    }

    public String getComment() {
        return "r1 r2 => applied intersection of relations r1 and r2";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        RippleValue rtrue = stack.getFirst();
        stack = stack.getRest();
        RippleValue rfalse = stack.getFirst();
        stack = stack.getRest();

        Operator inner = new Operator(new IntersectInner());
        solutions.put(
                stack.push(rtrue).push(Operator.OP).push(mc.valueOf(true)).push(inner));
        solutions.put(
                stack.push(rfalse).push(Operator.OP).push(mc.valueOf(false)).push(inner));
    }

    ////////////////////////////////////////////////////////////////////////////

    protected class IntersectInner implements StackMapping {
        private ListMemoizer<RippleValue, String> trueMemoizer = null;
        private ListMemoizer<RippleValue, String> falseMemoizer = null;

        public int arity() {
            // Require that the remainder of the stack (below the marker) is
            // in normal form.  This is somewhat arbitrary.
            return 2;
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            RippleValue marker = arg.getFirst();

            if (mc.toBoolean(marker)) {
                applyTrue(arg, solutions, mc);
            } else {
                applyFalse(arg, solutions, mc);
            }
        }

        private void applyTrue(final RippleList arg,
                               final Sink<RippleList> sink,
                               final ModelConnection mc) throws RippleException {
            RippleList stack = arg.getRest();

            if (null == trueMemoizer) {
                trueMemoizer = new ListMemoizer<RippleValue, String>(mc.getComparator());
            }

            trueMemoizer.put(stack, MEMO);

            if (null != falseMemoizer && null != falseMemoizer.get(stack)) {
                sink.put(stack);
            }
        }

        private void applyFalse(final RippleList arg,
                                final Sink<RippleList> sink,
                                final ModelConnection mc) throws RippleException {
            RippleList stack = arg.getRest();

            if (null == falseMemoizer) {
                falseMemoizer = new ListMemoizer<RippleValue, String>(mc.getComparator());
            }

            falseMemoizer.put(stack, MEMO);

            if (null != trueMemoizer && null != trueMemoizer.get(stack)) {
                sink.put(stack);
            }
        }

        public boolean isTransparent() {
            return true;
        }

        // TODO
        public StackMapping getInverse() throws RippleException {
            return new NullStackMapping();
        }
    }
}
