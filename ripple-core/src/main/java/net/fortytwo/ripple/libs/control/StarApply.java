/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.StarQuantifier;


/**
 * A primitive which optionally activates ("applies") the topmost item on the
 * stack.
 */
public class StarApply extends PrimitiveStackMapping {
    // TODO: arity should really be 1, but this is a nice temporary solution
    @Override
    public int arity() {
        return 2;
    }

    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessor, stack:starApply
                ControlLibrary.NS_2011_08 + "star-apply"};
    }

    public StarApply() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program to be executed", true)};
    }

    public String getComment() {
        return "p  => p*  -- optionally execute the program p any number of times";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        RippleValue first = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<Operator> opSink = new Sink<Operator>() {
            public void put(final Operator op) throws RippleException {
                solutions.put(rest.push(
                        new StackMappingWrapper(new StarQuantifier(op), mc)));
            }
        };

        Operator.createOperator(first, opSink, mc);
    }
}
