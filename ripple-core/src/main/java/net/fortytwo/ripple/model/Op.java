/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;

public class Op implements RippleValue {
    private final StackMapping inverseMapping = new StackMapping() {
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            RippleValue v;

            v = arg.getFirst();
            final RippleList rest = arg.getRest();

            Sink<Operator> opSink = new Sink<Operator>() {
                public void put(final Operator oper)
                        throws RippleException {
                    Operator inv = new Operator(oper.getMapping().getInverse());
                    solutions.put(rest.push(inv));
                }
            };

            Operator.createOperator(v, opSink, mc);
        }

        public StackMapping getInverse() throws RippleException {
            return getMapping();
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
            RippleValue v;

            v = arg.getFirst();
            final RippleList rest = arg.getRest();

            Sink<Operator> opSink = new Sink<Operator>() {
                public void put(final Operator oper)
                        throws RippleException {
                    solutions.put(rest.push(oper));
                }
            };

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

    public Op() {
    }

    public void printTo(final RipplePrintStream p)
            throws RippleException {
        System.err.println("You should not need to print op directly.");
        System.exit(1);
    }

    public RDFValue toRDF(final ModelConnection mc)
            throws RippleException {
        return mc.uriValue(Ripple.RIPPLE_ONTO_BASEURI + "op");
    }

    public StackMapping getMapping() {
        return mapping;
    }

    public boolean equals(final Object other) {
        return other instanceof Op;
    }

    public int hashCode() {
        // Arbitrary.
        return 1056205736;
    }

    public String toString() {
        return "op";
    }

    public Type getType() {
        // FIXME: the Operator class also uses this type
        return Type.OPERATOR;
    }
}

