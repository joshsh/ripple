/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;

public class Op implements RippleValue {
    private final StackMapping inverseMapping = new StackMapping() {
        public void apply(final StackContext arg,
                          final Sink<StackContext, RippleException> sink)
                throws RippleException {
            RippleValue v;
            RippleList stack = arg.getStack();

            v = stack.getFirst();
            final RippleList rest = stack.getRest();

            Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>() {
                public void put(final Operator oper)
                        throws RippleException {
                    Operator inv = new Operator(oper.getMapping().getInverse());
                    sink.put(arg.with(rest.push(inv)));
                }
            };

            Operator.createOperator(v, opSink, arg.getModelConnection());
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
        public void apply(final StackContext arg,
                          final Sink<StackContext, RippleException> sink)
                throws RippleException {
            RippleValue v;
            RippleList stack = arg.getStack();

            v = stack.getFirst();
            final RippleList rest = stack.getRest();

            Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>() {
                public void put(final Operator oper)
                        throws RippleException {
                    sink.put(arg.with(rest.push(oper)));
                }
            };

            Operator.createOperator(v, opSink, arg.getModelConnection());
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
        return mc.uriValue("http://fortytwo.net/2007/03/ripple/schema#op");
    }

    public boolean isActive() {
        return true;
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

