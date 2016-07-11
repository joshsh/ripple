package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.StackEvaluator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
// FIXME: this is a total hack
public class Count extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            // Note: this "count" has different semantics than the 2008-08 version
            StreamLibrary.NS_2013_03 + "count"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Count() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("mapping",
                        "a mapping to apply before counting",
                        false)
        };
    }

    public String getComment() {
        return "m -> m op, for which solutions are found and counted, then the result replaces m on the stack";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        // FIXME: cheat to temporarily disable asynchronous query answering
        boolean a = Ripple.asynchronousQueries();
        Ripple.enableAsynchronousQueries(false);
        try {
            Collector<RippleList> s = new Collector<>();
            StackEvaluator e = new LazyStackEvaluator();
            e.apply(arg.push(Operator.OP), s, mc);
            int count = s.size();

            solutions.accept(
                    arg.getRest().push(count));
        } finally {
            Ripple.enableAsynchronousQueries(a);
        }
    }
}
