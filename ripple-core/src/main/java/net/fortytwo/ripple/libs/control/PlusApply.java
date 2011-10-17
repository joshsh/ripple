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
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.PlusQuantifier;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;


/**
 * A primitive which activates ("applies") the topmost item on the stack one or
 * more times.
 */
public class PlusApply extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessor, stack:plusApply
                ControlLibrary.NS_2011_08 + "plus-apply"};
    }

    public PlusApply() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program to be executed", true)};
    }

    public String getComment() {
        return "p  =>  p+  -- execute the program p at least one time, and up to any number of times";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        RippleValue first = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<Operator> opSink = new Sink<Operator>() {
            public void put(final Operator op) throws RippleException {
                solutions.put(arg.with(rest.push(
                        new StackMappingWrapper(new PlusQuantifier(op), mc))));
            }
        };

        Operator.createOperator(first, opSink, arg.getModelConnection());
    }
}
