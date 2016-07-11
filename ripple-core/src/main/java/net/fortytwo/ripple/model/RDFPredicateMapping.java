package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFPredicateMapping implements StackMapping {
    private static final Logger logger = LoggerFactory.getLogger(RDFPredicateMapping.class.getName());

    private static final int ARITY = 1;

    // Note: only the types SP_O and OP_S are supported for now
    private final StatementPatternQuery.Pattern type;

    private final Value predicate;
    private final Value context;

    public RDFPredicateMapping(final StatementPatternQuery.Pattern type,
                               final Value pred,
                               final Value context) {
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

    private void findListPredicateSolutions(final Object subject,
                                            final RippleList rest,
                                            final Sink<RippleList> solutions,
                                            final ModelConnection mc) throws RippleException {
        if (subject instanceof RippleList) {
            if (predicate.equals(RDF.TYPE)) {
                solutions.accept(rest.push(mc.valueOf(URI.create(RDF.LIST.toString()))));
            } else if (!((RippleList) subject).isNil()) {
                //System.out.println("" + subject + " " + predicate);
                if (predicate.equals(RDF.FIRST)) {
                    Object f = ((RippleList) subject).getFirst();
                    solutions.accept(rest.push(f));
                } else if (predicate.equals(RDF.REST)) {
                    RippleList r = ((RippleList) subject).getRest();
                    solutions.accept(rest.push(r));
                }
            }
        }
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        Object sourceVal = arg.getFirst();
        StatementPatternQuery query;

        switch (this.type) {
            case SP_O:
                findListPredicateSolutions(sourceVal, arg.getRest(), solutions, mc);

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

        Sink<Object> resultSink = new ValueSink(arg, solutions);

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

    private class ValueSink implements Sink<Object> {
        private final Sink<RippleList> sink;
        private final RippleList arg;

        public ValueSink(final RippleList arg, final Sink<RippleList> sink) {
            this.arg = arg;
            this.sink = sink;
        }

        public void accept(final Object v) throws RippleException {
            try {
                //System.out.println("got Ripple value: " + v);
                // Note: relies on this mapping's arity being equal to 1
                sink.accept(arg.getRest().push(v));
            } catch (RippleException e) {
                // Soft fail
                logger.warn("failed to put solution", e);
            }
        }
    }
}
