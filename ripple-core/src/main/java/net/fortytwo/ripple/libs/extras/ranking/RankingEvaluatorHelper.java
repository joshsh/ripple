package net.fortytwo.ripple.libs.extras.ranking;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.ListMemoizer;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Closure;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleValueComparator;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Note: for synchronous evaluation only.
 * <p/>
 * User: josh
 * Date: 4/4/11
 * Time: 9:28 AM
 */
public class RankingEvaluatorHelper {
    private static final Logger LOGGER = Logger.getLogger(RankingEvaluatorHelper.class);

    // A prioritized queue of active (still-to-be-reduced) stacks
    private final PriorityQueue<RankingContext> queue;

    // A ranked list of intermediate results
    private final List<RankingContext> resultList;

    // A set of intermediate results.
    private final ListMemoizer<RippleValue, RankingContext> resultMemos;

    public RankingEvaluatorHelper(final StackContext arg) {
        queue = new PriorityQueue<RankingContext>(1, comparator);

        resultList = new LinkedList<RankingContext>();
        resultMemos = new ListMemoizer<RippleValue, RankingContext>(
                new RippleValueComparator(arg.getModelConnection()));

        handleOutput(new RankingContext(arg.getStack(), arg.getModelConnection()));
    }

    /**
     * Spends one computational step on improving the ranking.
     *
     * @return whether it is possible to take further steps
     * @throws RippleException if the ranking can't be computed
     */
    public boolean reduceOnce() throws RippleException {
        //System.out.println("\treduceOnce()");
        if (!queue.isEmpty()) {
            //System.out.println("\t\tqueue NOT empty");
            //inputSink.put(queue.poll());
            reduce(queue.poll());

            return !queue.isEmpty();
        } else {
            //System.out.println("\t\tqueue is empty");
            return false;
        }
    }

    /**
     * @return an ordered list of ranking results
     */
    public List<RankingContext> getResults() {
        Collections.sort(resultList, comparator);
        //for (RankingContext c : resultList) {
        //    System.out.println("\tresult: " + c);
        //}
        return resultList;
    }

    private void handleOutput(final RankingContext c) {
        //System.out.println("adding intermediate: " + c.getStack());
        if (c.getStack().isNil() || !c.getStack().getFirst().isActive()) {
            RankingContext other = resultMemos.get(c.getStack());

            if (null == other) {
                resultMemos.put(c.getStack(), c);
                resultList.add(c);
            } else {
                other.setPriority(other.getPriority() + c.getPriority());
            }
        } else {
            queue.add(c);
        }
    }

    private final Comparator<RankingContext> comparator = new Comparator<RankingContext>() {
        public int compare(final RankingContext first,
                           final RankingContext second) {
            // This orders items from highest to lowest priority.
            return ((Double) second.getPriority()).compareTo(first.getPriority());
        }
    };

    private final Sink<StackContext, RippleException> outputSink = new Sink<StackContext, RippleException>() {
        public void put(final StackContext c) throws RippleException {
            //System.out.println("got this output: " + c.getStack());
            if (c instanceof RankingContext) {
                handleOutput((RankingContext) c);
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////

    private void reduce(final StackContext arg) throws RippleException {
        RippleList stack = arg.getStack();
        RippleList ops = arg.getModelConnection().list();

        while (true) {
            RippleValue first = stack.getFirst();

            if (stack.isNil() || !first.isActive()) {
                if (ops.isNil()) {
                    outputSink.put(arg.with(stack));
                    return;
                } else {
                    Closure c = new Closure(((Operator) ops.getFirst()).getMapping(), first);
                    stack = stack.getRest().push(new Operator(c));
                    ops = ops.getRest();
                }
            } else {
                StackMapping f = ((Operator) first).getMapping();

                if (0 == f.arity()) {
                    try {
                        if (ops.isNil()) {
                            f.apply(arg.with(stack.getRest()), outputSink);
                        } else {
                            f.apply(arg.with(stack.getRest()), new SpecialSink(ops));
                        }
                    } catch (Throwable t) {
                        // To keep things simple, just eat any errors.
                        LOGGER.error("error in expression reduction", t);
                    }
                    return;
                } else {
                    ops = ops.push(first);
                    stack = stack.getRest();
                }
            }
        }
    }

    private class SpecialSink implements Sink<StackContext, RippleException> {
        private final RippleList ops;

        public SpecialSink(final RippleList ops) {
            this.ops = ops;
        }

        public void put(final StackContext arg) throws RippleException {
            RippleList stack = arg.getStack();

            RippleList cur = ops;
            while (!cur.isNil()) {
                stack = stack.push(cur.getFirst());
                cur = cur.getRest();
            }

            reduce(arg.with(stack));
        }
    }
}
