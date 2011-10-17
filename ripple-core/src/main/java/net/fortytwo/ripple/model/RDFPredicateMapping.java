/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/model/RDFPredicateMapping.java $
 * $Revision: 60 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.Ripple;
import org.openrdf.model.vocabulary.RDF;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:29:26 AM
 */
public class RDFPredicateMapping implements StackMapping {
    private static final int ARITY = 1;

    // Note: only the types SP_O and OP_S are supported for now
    private final StatementPatternQuery.Pattern type;

    private final RDFValue predicate;
    private final RDFValue context;

    public RDFPredicateMapping(final StatementPatternQuery.Pattern type,
                               final RDFValue pred,
                               final RDFValue context) {
        this.type = type;
        this.predicate = pred;
        this.context = context;
    }

    public int arity() {
        return ARITY;
    }

    public boolean isTransparent() {
        return true;
    }

    private void findListPredicateSolutions(final RippleValue subject,
                                            final StackContext arg,
                                            final RippleList rest,
                                            final Sink<StackContext> solutions) throws RippleException {
        if (subject instanceof RippleList) {
            if (predicate.sesameValue().equals(RDF.TYPE)) {
                solutions.put(arg.with(rest.push(arg.getModelConnection().uriValue(RDF.LIST.toString()))));
            } else if (!((RippleList) subject).isNil()) {
                //System.out.println("" + subject + " " + predicate);
                if (predicate.sesameValue().equals(RDF.FIRST)) {
                    RippleValue f = ((RippleList) subject).getFirst();
                    solutions.put(arg.with(rest.push(f)));
                } else if (predicate.sesameValue().equals(RDF.REST)) {
                    RippleList r = ((RippleList) subject).getRest();
                    solutions.put(arg.with(rest.push(r)));
                }
            }
        }
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions) throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        RippleValue sourceVal = stack.getFirst();
        StatementPatternQuery query;

        switch (this.type) {
            case SP_O:
                findListPredicateSolutions(sourceVal, arg, stack.getRest(), solutions);

                query = (null == context)
                        ? new StatementPatternQuery(sourceVal, predicate, null)
                        : new StatementPatternQuery(sourceVal, predicate, null, context);
                break;
            case PO_S:
                query = (null == context)
                        ? new StatementPatternQuery(null, predicate, sourceVal)
                        : new StatementPatternQuery(null, predicate, sourceVal, context);
                break;
            default:
                throw new RippleException("unsupported query type: " + type);
        }

        Sink<RippleValue> resultSink = new ValueSink(arg, solutions);

        //System.out.println("asynch: " + Ripple.asynchronousQueries());
        if (Ripple.asynchronousQueries()) {
            mc.query(query, resultSink, true);
        } else {
            mc.query(query, resultSink, false);
        }
    }

    public String toString() {
        return "Predicate(" + predicate + ")";
    }

    public StackMapping getInverse() throws RippleException {
        StatementPatternQuery.Pattern inverseType;

//System.out.println("inverting RDF predicate mapping with predicate " + predicate + " and context " + context);
        switch (this.type) {
            case SP_O:
                inverseType = StatementPatternQuery.Pattern.PO_S;
                break;
            case PO_S:
                inverseType = StatementPatternQuery.Pattern.SP_O;
                break;
            default:
                throw new RippleException("unsupported query type: " + type);
        }

        return new RDFPredicateMapping(inverseType, this.predicate, this.context);
    }

    private class ValueSink implements Sink<RippleValue> {
        private Sink<StackContext> sink;
        private StackContext arg;

        public ValueSink(final StackContext arg, final Sink<StackContext> sink) {
            this.arg = arg;
            this.sink = sink;
        }

        public void put(final RippleValue v) throws RippleException {
            try {
                // Note: relies on this mapping's arity being equal to 1
                sink.put(arg.with(arg.getStack().getRest().push(v)));
            } catch (RippleException e) {
                // Soft fail
                e.logError();
            }
        }
    }
}
