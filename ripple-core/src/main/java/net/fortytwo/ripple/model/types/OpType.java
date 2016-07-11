package net.fortytwo.ripple.model.types;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Op;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

import java.net.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class OpType extends SimpleType<Op> {

    public OpType() {
        super(Op.class);
    }

    private final StackMapping inverseMapping = new StackMapping() {
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            Object v;

            v = arg.getFirst();
            final RippleList rest = arg.getRest();

            Sink<Operator> opSink = oper -> {
                Operator inv = new Operator(oper.getMapping().getInverse());
                solutions.accept(rest.push(inv));
            };

            Operator.createOperator(v, opSink, mc);
        }

        public StackMapping getInverse() throws RippleException {
            return mapping;
        }

        @Override
        public String toString() {
            return "opInverse";
        }

        public int arity() {
            return 1;
        }


        public boolean isTransparent() {
            return true;
        }
    };

    private final StackMapping mapping = new StackMapping() {
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            Object v;

            v = arg.getFirst();
            final RippleList rest = arg.getRest();

            Sink<Operator> opSink = oper -> solutions.accept(rest.push(oper));

            Operator.createOperator(v, opSink, mc);
        }

        public int arity() {
            return 1;
        }


        public boolean isTransparent() {
            return true;
        }


        public StackMapping getInverse() throws RippleException {
            return inverseMapping;
        }
    };

    @Override
    public boolean isInstance(Op instance) {
        return true;
    }

    @Override
    public Value toRDF(Op instance, ModelConnection mc) throws RippleException {
        return mc.valueOf(URI.create(Ripple.RIPPLE_ONTO_BASEURI + "op"));
    }

    @Override
    public StackMapping getMapping(Op instance) {
        return mapping;
    }

    @Override
    public void print(Op instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        throw new IllegalStateException("You should not need to print op directly.");
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.OPERATOR;
    }

    @Override
    public int compare(Op o1, Op o2, ModelConnection mc) {
        // there is only one Op
        return 0;
    }
}
