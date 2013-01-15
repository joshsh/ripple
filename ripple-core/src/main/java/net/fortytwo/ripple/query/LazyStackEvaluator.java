package net.fortytwo.ripple.query;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Closure;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;

// Note: not thread-safe, on account of stop()

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LazyStackEvaluator extends StackEvaluator {

    private boolean stopped = true;

    protected class EvaluatorSink implements Sink<RippleList> {
        private Sink<RippleList> sink;
        private final ModelConnection mc;

        public EvaluatorSink(final Sink<RippleList> sink,
                             final ModelConnection mc) {
            this.sink = sink;
            //System.out.println(this + "( " + sink + ")");
            this.mc = mc;
        }

        public void put(final RippleList arg)
                throws RippleException {
            if (stopped) {
                return;
            }

            //System.out.println(this + " -- stack = " + stack);
            RippleValue first = arg.getFirst();
            //System.out.println( "   first.isActive() = " + first.isActive() );
            //System.out.println("   first = " + stack.getFirst());

            if (null != first.getMapping()) {
                RippleList rest = arg.getRest();
                //System.out.println("   rest = " + rest);

                final StackMapping f = first.getMapping();
                //System.out.println("   f = " + f);
                //System.out.println("   f.arity() = " + f.arity());

                // Nullary functions don't need their argument stacks reduced.
                // They shouldn't even care if the stack is empty.
                if (f.arity() == 0) {
                    f.apply(rest, this, mc);
                }

                // Functions with positive arity do require the stack to be
                // reduced, to one level per argument.
                else {
                    // We simply ignore stacks which can't be reduced to
                    // something with a passive item on top.
                    if (rest.isNil()) {
                        //						sink.put( stack );
                    } else {
                        final Sink<RippleList> thisEval = this;
                        Sink<RippleList> argSink = new Sink<RippleList>() {
                            public void put(final RippleList arg) throws RippleException {
                                Closure c = new Closure(f, arg.getFirst());
                                new EvaluatorSink(thisEval, mc).put(arg.getRest().push(new Operator(c)));
                            }
                        };

                        // Reduce the argument portion of the stack.
                        new EvaluatorSink(argSink, mc).put(arg.getRest());
                    }
                }
            } else {
                sink.put(arg);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        if (arg.isNil()) {
            return;
        }

        /*
        Sink<StackContext, RippleException> debugSink = new Sink<StackContext, RippleException>() {
            public void put(final StackContext ctx) throws RippleException {
                System.out.println("yielding value: " + ctx.getStack());
                solutions.put(ctx);
            }
        };
        //*/

        EvaluatorSink evalSink = new EvaluatorSink(solutions, mc);
        //EvaluatorSink evalSink = new EvaluatorSink( debugSink );
        stopped = false;

        try {
            evalSink.put(arg);
        }

        // Attempt to recover from stack overflow.
        catch (StackOverflowError e) {
            throw new RippleException(e);
        }
    }

    public void stop() {
        synchronized (this) {
            stopped = true;
        }
    }
}

