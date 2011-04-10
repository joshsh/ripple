/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.libs.extras.ExtrasLibrary;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

public class OperatorAST implements AST<RippleList> {
    public enum Type {
        Apply,
        Inverse,
        Option,
        Plus,
        Range,
        Star,
        Times,
    }

    private final Type type;
    private NumberAST min, max;

    public OperatorAST(final Type type) {
        this.type = type;
    }

    public OperatorAST() {
        this(Type.Apply);
    }

    public OperatorAST(final NumberAST times) {
        type = Type.Times;
        min = times;
    }

    public OperatorAST(final NumberAST min, final NumberAST max) {
        type = Type.Range;
        this.min = min;
        this.max = max;
    }

    public void evaluate(final Sink<RippleList, RippleException> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        RippleList l;

        switch (type) {
            case Apply:
                l = mc.list(Operator.OP);
                break;
            case Inverse:
                // Note: only one "op" here.  This merely finds the inverse; it doesn't automatically apply the inverse.
                l = mc.list(Operator.OP)
                        .push(ExtrasLibrary.getInvertValue());
                break;
            case Option:
                l = mc.list(Operator.OP)
                        .push(Operator.OP)
                        .push(StackLibrary.getOptionApplyValue());
                break;
            case Plus:
                l = mc.list(Operator.OP)
                        .push(Operator.OP)
                        .push(StackLibrary.getPlusApplyValue());
                break;
            case Range:
                l = mc.list(Operator.OP)
                        .push(Operator.OP)
                        .push(StackLibrary.getRangeApplyValue())
                        .push(max.getValue(mc))
                        .push(min.getValue(mc));
                break;
            case Star:
                l = mc.list(Operator.OP)
                        .push(Operator.OP)
                        .push(StackLibrary.getStarApplyValue());
                break;
            case Times:
                l = mc.list(Operator.OP)
                        .push(Operator.OP)
                        .push(StackLibrary.getTimesApplyValue())
                        .push(min.getValue(mc));
                break;
            default:
                throw new RippleException("unhandled operator type: " + type);
        }

        sink.put(l);
    }

    public String toString() {
        switch (type) {
            case Apply:
                return ".";
            case Inverse:
                return "~";
            case Option:
                return "?";
            case Plus:
                return "+";
            case Range:
                return "{" + min + "," + max + "}";
            case Star:
                return "*";
            case Times:
                return "{" + min + "}";
            default:
                return "error";
        }
    }
}

