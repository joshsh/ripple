package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.ListMemoizer;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A filter which drops any stack which has already been transmitted and behaves
 * like the identity filter otherwise, making a stream of stacks into a set of
 * stacks.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Distinct extends PrimitiveStackMapping {
    private static final String MEMO = "memo";

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2013_03 + "distinct",
            StreamLibrary.NS_2008_08 + "distinct",
            StreamLibrary.NS_2008_08 + "unique",
            StreamLibrary.NS_2007_08 + "unique",
            StreamLibrary.NS_2007_05 + "unique"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Distinct()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{};
    }

    public String getComment() {
        return "transmits stacks at most once, as determined by list comparison";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        solutions.accept(
                arg.push(
                        new Operator(
                                new DistinctInner())));
    }

    protected class DistinctInner implements StackMapping {
        private ListMemoizer<Object, String> memoizer = null;

        public int arity() {
            return 1;
        }

        // Note: consecutive calls to applyTo should reference the same Model.
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            if (null == memoizer) {
                memoizer = new ListMemoizer<Object, String>(mc.getComparator());
            }

            if (memoizer.put(arg, MEMO)) {
                solutions.accept(arg);
            }
        }

        public boolean isTransparent() {
            return true;
        }

        public StackMapping getInverse() throws RippleException {
            return new NullStackMapping();
        }
    }
}

