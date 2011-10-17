/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.NullStackMapping;

/**
 * A primitive which consumes a numeric "arity" and produces an active identity
 * filter with the given arity.  This forces the remainder of the stack to be
 * reduced to the corresponding depth.
 */
public class Ary extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2011_08 + "ary",
                StackLibrary.NS_2008_08 + "ary",
                StackLibrary.NS_2007_08 + "ary",
                StackLibrary.NS_2007_05 + "ary"};
    }

    public Ary()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("n", "the (minimum) arity of the resulting function", true)};
    }

    public String getComment() {
        return "n  =>  f -- where f is an n-ary version of the id function";
    }

    private class NaryId implements StackMapping {
        private int n;

        public NaryId(final int arity) {
            n = arity;
        }

        public int arity() {
            return n;
        }

        public void apply(final StackContext arg,
                          final Sink<StackContext> sink
        )
                throws RippleException {
            sink.put(arg);
        }

        public boolean isTransparent() {
            return true;
        }

        // TODO
        public StackMapping getInverse() throws RippleException {
            return new NullStackMapping();
        }

        public String toString() {
            return "NaryId(" + n + ")";
        }
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        int n;

        n = mc.toNumericValue(stack.getFirst()).intValue();
        stack = stack.getRest();

        solutions.put(arg.with(
                stack.push(new Operator(new NaryId(n)))));
    }
}

