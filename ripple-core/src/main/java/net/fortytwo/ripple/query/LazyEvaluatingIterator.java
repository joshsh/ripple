package net.fortytwo.ripple.query;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Closure;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * A "pull-based" program evaluator which uses Ripple's lazy evaluation strategy but generates solutions
 * incrementally based on calls to next(), rather than pushing all solutions to a stream.
 * <p/>
 * Note: this iterator is for single-threaded evaluation only.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LazyEvaluatingIterator implements CloseableIteration<RippleList, RippleException> {

    private final List<RippleList> intermediates = new LinkedList<RippleList>();
    private final List<RippleList> solutions = new LinkedList<RippleList>();
    private final ModelConnection mc;

    public LazyEvaluatingIterator(final RippleList stack,
                                  final ModelConnection mc) {
        this.mc = mc;

        // TODO: refusing to evaluate nil stacks makes this iterator like LazyStackEvaluator, but is this really the desired behavior?
        if (!stack.isNil()) {
            //System.out.println("adding initial intermediate: " + stack);
            intermediates.add(stack);
        }
    }

    @Override
    public boolean hasNext() throws RippleException {
        while (true) {
            if (solutions.size() > 0) {
                return true;
            } else if (intermediates.size() > 0) {
                reduce(intermediates.remove(0));
            } else {
                return false;
            }
        }
    }

    @Override
    public RippleList next() {
        return solutions.remove(0);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void reduce(final RippleList stack) throws RippleException {

        //System.out.println("evaluating: " + stack);
        RippleList left = stack;
        RippleList right = mc.list();

        while (true) {
            //System.out.println("--");
            //System.out.println("\tleft: " + left);
            //System.out.println("\tright: " + right);

            if (left.isNil()) {
                if (right.isNil()) {
                    //System.out.println("adding solution: " + left);
                    solutions.add(left);
                }

                return;
            }
            RippleValue first = left.getFirst();
            final StackMapping f = first.getMapping();

            if (null == f) {
                if (right.isNil()) {
                    //System.out.println("adding solution: " + left);
                    solutions.add(left);
                    return;
                } else {
                    Closure c = new Closure(right.getFirst().getMapping(), first);
                    right = right.getRest();
                    left = left.getRest().push(new Operator(c));
                }
            } else {
                if (0 == f.arity()) {
                    Collector<RippleList> results = new Collector<RippleList>();
                    // Note: synchronous evaluation is required
                    // Note: stack context is trivial
                    f.apply(left.getRest(), results, mc);
                    for (RippleList s : results) {
                        RippleList i = s;
                        RippleList cur = right;
                        while (!cur.isNil()) {
                            i = i.push(cur.getFirst());
                            cur = cur.getRest();
                        }

                        //System.out.println("adding intermediate: " + i);
                        intermediates.add(i);
                    }
                    return;
                } else {
                    right = right.push(first);
                    left = left.getRest();
                }
            }
        }
    }

    @Override
    public void close() throws RippleException {
        // Do nothing.  We implement CloseableIteration simply so we can throw exceptions.
    }

    public static class WrappingEvaluator extends StackEvaluator {
        private boolean stopped;

        @Override
        public void stop() {
            stopped = true;
        }

        @Override
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            try {
                CloseableIteration<RippleList, RippleException> iter
                        = new LazyEvaluatingIterator(arg, mc);
                try {
                    while (iter.hasNext() && !stopped) {
                        solutions.put(iter.next());
                    }
                } finally {
                    iter.close();
                }
            } finally {
                stopped = false;
            }
        }
    }
}
