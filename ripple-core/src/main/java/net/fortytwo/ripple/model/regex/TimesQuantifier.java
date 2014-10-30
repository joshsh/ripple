package net.fortytwo.ripple.model.regex;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TimesQuantifier implements StackMapping {
    private final Operator innerOperator;
    private final int min, max;

    public TimesQuantifier(final Operator oper, final int min, final int max) {
        this.innerOperator = oper;
        this.min = min;
        this.max = max;
    }

    public int arity() {
        // TODO
        return 1;
    }

    public boolean isTransparent() {
        return innerOperator.getMapping().isTransparent();
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        if (0 == min) {
            solutions.put(arg);
        }

        if (max > 0) {
            if (1 == max) {
                solutions.put(arg.push(innerOperator));
            } else {
                int newMin = (0 == min) ? 0 : min - 1, newMax = max - 1;

                solutions.put(arg
                        .push(innerOperator)
                        .push(new Operator(new TimesQuantifier(innerOperator, newMin, newMax))));
            }
        }
    }

    public StackMapping getInverse() throws RippleException {
        return new NullStackMapping();
    }
}
