package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;

/**
 * User: josh
 * Date: 5/14/11
 * Time: 4:00 PM
 */
public class ListGenerator extends Operator {
    public ListGenerator(final Source<RippleList, RippleException> source) {
        super(createMapping(source));
    }

    private static StackMapping createMapping(final Source<RippleList, RippleException> source) {
        return new StackMapping(){
            public int arity() {
                return 0;
            }

            public StackMapping getInverse() throws RippleException {
                return new NullStackMapping();
            }

            public boolean isTransparent() {
                return true;
            }

            public void apply(final StackContext arg,
                              final Sink<StackContext, RippleException> solutions) throws RippleException {
                Sink<RippleList, RippleException> s = new Sink<RippleList, RippleException>() {
                    public void put(final RippleList l) throws RippleException {
                        solutions.put(arg.with(arg.getStack().push(l.invert())));
                    }
                };

                source.writeTo(s);
            }
        };
    }
}
