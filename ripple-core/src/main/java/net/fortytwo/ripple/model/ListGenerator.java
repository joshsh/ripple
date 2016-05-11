package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ListGenerator extends Operator {
    public ListGenerator(final Source<RippleList> source) {
        super(createMapping(source));
    }

    private static StackMapping createMapping(final Source<RippleList> source) {
        return new StackMapping() {
            public int arity() {
                return 0;
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
                Sink<RippleList> s = new Sink<RippleList>() {
                    public void accept(final RippleList l) throws RippleException {
                        solutions.accept(arg.push(l.invert()));
                    }
                };

                source.writeTo(s);
            }
        };
    }
}
