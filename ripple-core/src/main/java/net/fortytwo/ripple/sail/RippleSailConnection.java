package net.fortytwo.ripple.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.control.ControlLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import net.fortytwo.ripple.query.StackEvaluator;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailConnectionWrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * User: josh
 * Date: 6/2/11
 * Time: 12:53 PM
 */
public class RippleSailConnection extends SailConnectionWrapper {
    private final ModelConnection modelConnection;
    private final StackEvaluator evaluator;
    private final RippleValueFactory valueFactory;

    public RippleSailConnection(final ModelConnection modelConnection,
                                final StackEvaluator evaluator,
                                final RippleValueFactory valueFactory) {
        super(((SesameModelConnection) modelConnection).getSailConnection());
        this.modelConnection = modelConnection;
        this.evaluator = evaluator;
        this.valueFactory = valueFactory;
    }

    @Override
    public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(
            final TupleExpr query,
            final Dataset dataset,
            final BindingSet bindings,
            final boolean includeInferred) throws SailException {
        try {
            TripleSource tripleSource = new SailConnectionTripleSource(this, valueFactory, includeInferred);
            EvaluationStrategyImpl strategy = new EvaluationStrategyImpl(tripleSource, dataset);
            return strategy.evaluate(query, bindings);
        } catch (QueryEvaluationException e) {
            throw new SailException(e);
        }
    }

    @Override
    public CloseableIteration<? extends Resource, SailException> getContextIDs() throws SailException {
        // When implementing this method, make sure that only RippleSesameValues are passed up
        throw new UnsupportedOperationException();
    }

    @Override
    public void addStatement(final Resource subject, final URI predicate, final Value object, final Resource... contexts) throws SailException {
        // When implementing this method, make sure that only RippleSesameValues are passed down
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeStatements(final Resource subject, final URI predicate, final Value object, final Resource... contexts) throws SailException {
        // Implement this method if/when addStatement is implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear(final Resource... contexts) throws SailException {
        // Implement this method if/when addStatement is implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public CloseableIteration<? extends Statement, SailException> getStatements(
            Resource subject,
            final URI predicate,
            Value object,
            final boolean includeInferred,
            final Resource... contexts) throws SailException {
        //System.out.println("getStatements(" + subject + ", " + predicate + ", " + object + " [, ...])");

        try {
            // An "all wildcards" query, or a query with a wildcard predicate, just goes to the base SailConnection
            if (null == predicate || (null == subject && null == object)) {
                return getWrappedConnection().getStatements(subject, predicate, object, includeInferred, contexts);
            }

            // Forward traversal
            else if (null != subject && null == object) {
                if (!(subject instanceof RippleSesameValue)) {
                    subject = (Resource) valueFactory.nativize(subject);
                }
                RippleList stack = ((RippleSesameValue) subject).getStack();
                if (null == stack) {
                    stack = modelConnection.list().push(modelConnection.canonicalValue(new RDFValue(subject)));

                    // Note: this may or may not be worth the extra CPU cycles.
                    ((RippleSesameValue) subject).setStack(stack);
                }

                stack = stack.push(modelConnection.canonicalValue(new RDFValue(predicate))).push(Operator.OP);
                //System.out.println("stack = " + stack);

                Collector<StackContext, RippleException> solutions = new Collector<StackContext, RippleException>();
                evaluator.apply(new StackContext(stack, modelConnection), solutions);

                Collection<RippleList> stacks = new LinkedList<RippleList>();
                for (StackContext c : solutions) {
                    //System.out.println("solution: " + c);
                    RippleList s = c.getStack();
                    if (!s.isNil()) {
                        stacks.add(s);
                    }
                }

                return new SolutionIteration(stacks, false, subject, predicate, object, contexts);
            }

            // Backward traversal
            else if (null == subject && null != object) {
                if (!(object instanceof RippleSesameValue)) {
                    object = valueFactory.nativize(object);
                }
                RippleList stack = ((RippleSesameValue) object).getStack();
                if (null == stack) {
                    stack = modelConnection.list().push(modelConnection.canonicalValue(new RDFValue(object)));

                    // Note: this may or may not be worth the extra CPU cycles.
                    ((RippleSesameValue) object).setStack(stack);
                }

                stack = stack.push(modelConnection.canonicalValue(new RDFValue(predicate)))
                        .push(ControlLibrary.getInverseValue())
                        .push(Operator.OP)
                        .push(Operator.OP);
                //System.out.println("stack = " + stack);

                Collector<StackContext, RippleException> solutions = new Collector<StackContext, RippleException>();
                evaluator.apply(new StackContext(stack, modelConnection), solutions);

                Collection<RippleList> stacks = new LinkedList<RippleList>();
                for (StackContext c : solutions) {
                    //System.out.println("solution: " + c);
                    RippleList s = c.getStack();
                    if (!s.isNil()) {
                        stacks.add(s);
                    }
                }

                return new SolutionIteration(stacks, true, subject, predicate, object, contexts);
            } else if (null != predicate && null != subject && null != object) {
                throw new IllegalStateException("this pattern is not yet implemented!");
            } else {
                throw new IllegalStateException();
            }
        } catch (RippleException e) {
            throw new SailException(e);
        }
    }

    private class SolutionIteration implements CloseableIteration<Statement, SailException> {
        private final Iterator<RippleList> iter;
        private final boolean inverse;
        private final Resource subject;
        private final URI predicate;
        private final Value object;
        private final Resource[] contexts;

        public SolutionIteration(Collection<RippleList> coll,
                                 boolean inverse,
                                 Resource subject,
                                 URI predicate,
                                 Value object,
                                 Resource... contexts) {
            this.iter = coll.iterator();
            this.inverse = inverse;
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
            this.contexts = contexts;
        }


        @Override
        public void close() throws SailException {
            // Do nothing.
        }

        @Override
        public boolean hasNext() throws SailException {
            return iter.hasNext();
        }

        @Override
        public Statement next() throws SailException {
            try {
                RippleList stack = iter.next();

                if (inverse) {
                    Value subj = stack.getFirst().toRDF(modelConnection).sesameValue();
                    RippleSesameValue s = (RippleSesameValue) valueFactory.nativize(subj);
                    s.setStack(stack);

                    return valueFactory.createStatement((Resource) s, predicate, object);
                } else {
                    Value obj = stack.getFirst().toRDF(modelConnection).sesameValue();
                    RippleSesameValue o = (RippleSesameValue) valueFactory.nativize(obj);
                    o.setStack(stack);

                    return valueFactory.createStatement(subject, predicate, (Value) o);
                }
            } catch (RippleException e) {
                throw new SailException(e);
            }
        }

        @Override
        public void remove() throws SailException {
            throw new UnsupportedOperationException();
        }
    }
}
