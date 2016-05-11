package net.fortytwo.ripple.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.control.ControlLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import net.fortytwo.ripple.query.LazyEvaluatingIterator;
import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.EvaluationStrategy;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.query.algebra.evaluation.impl.SimpleEvaluationStrategy;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailConnectionWrapper;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleSailConnection extends SailConnectionWrapper {
    private final ModelConnection modelConnection;
    private final RippleValueFactory valueFactory;

    public RippleSailConnection(final ModelConnection modelConnection,
                                final RippleValueFactory valueFactory) {
        super(((SesameModelConnection) modelConnection).getSailConnection());
        this.modelConnection = modelConnection;
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
            EvaluationStrategy strategy = new SimpleEvaluationStrategy(tripleSource, dataset, null);
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
    public void addStatement(final Resource subject,
                             final IRI predicate,
                             final Value object,
                             final Resource... contexts) throws SailException {
        // When implementing this method, make sure that only RippleSesameValues are passed down
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeStatements(final Resource subject,
                                 final IRI predicate,
                                 final Value object,
                                 final Resource... contexts) throws SailException {
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
            IRI predicate,
            Value object,
            final boolean includeInferred,
            final Resource... contexts) throws SailException {
        //System.out.println("getStatements(" + subject + ", " + predicate + ", " + object + " [, ...])");

        try {
            //if (null != predicate) {
            //    predicate = (IRI) valueFactory.nativize(predicate);
            //}

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
                //System.out.println("\tstack = " + stack);
                if (null == stack) {
                    stack = modelConnection.list().push(modelConnection.canonicalValue(subject));

                    // Note: this may or may not be worth the extra CPU cycles.
                    ((RippleSesameValue) subject).setStack(stack);
                }

                stack = stack.push(modelConnection.canonicalValue(
                        valueFactory.nativize(predicate))).push(Operator.OP);
                //System.out.println("\t\tstack (new) = " + stack);

                CloseableIteration<RippleList, RippleException> solutions
                        = new LazyEvaluatingIterator(stack, modelConnection);
                /*
                Collector<StackContext, RippleException> solutions = new Collector<StackContext, RippleException>();
                evaluator.apply(new StackContext(stack, modelConnection), solutions);

                Collection<RippleList> stacks = new LinkedList<RippleList>();
                for (StackContext c : solutions) {
                    //System.out.println("\tsolution: " + c);
                    RippleList s = c.getStack();
                    if (!s.isNil()) {
                        stacks.add(s);
                    }
                }

                return new SolutionIteration(stacks.iterator(), false, subject, predicate, object, contexts);
                */

                return new SolutionIteration(solutions, false, subject, predicate, object, contexts);
            }

            // Backward traversal
            else if (null == subject) {
                if (!(object instanceof RippleSesameValue)) {
                    object = valueFactory.nativize(object);
                }
                RippleList stack = ((RippleSesameValue) object).getStack();
                if (null == stack) {
                    stack = modelConnection.list().push(modelConnection.canonicalValue(object));

                    // Note: this may or may not be worth the extra CPU cycles.
                    ((RippleSesameValue) object).setStack(stack);
                }

                stack = stack.push(modelConnection.canonicalValue(predicate))
                        .push(ControlLibrary.getInverseValue())
                        .push(Operator.OP)
                        .push(Operator.OP);
                //System.out.println("stack = " + stack);

                CloseableIteration<RippleList, RippleException> solutions
                        = new LazyEvaluatingIterator(stack, modelConnection);
                /*
                Collector<StackContext, RippleException> solutions = new Collector<StackContext, RippleException>();
                evaluator.apply(new StackContext(stack, modelConnection), solutions);

                Collection<RippleList> stacks = new LinkedList<RippleList>();
                for (StackContext c : solutions) {
                    //System.out.println("\tsolution: " + c);
                    RippleList s = c.getStack();
                    if (!s.isNil()) {
                        stacks.add(s);
                    }
                }

                return new SolutionIteration(stacks.iterator(), false, subject, predicate, object, contexts);
                */

                return new SolutionIteration(solutions, true, subject, predicate, object, contexts);
            } else {
                return getWrappedConnection().getStatements(subject, predicate, object, includeInferred, contexts);
            }
        } catch (RippleException e) {
            throw new SailException(e);
        }
    }

    private class SolutionIteration implements CloseableIteration<Statement, SailException> {
        private final CloseableIteration<RippleList, RippleException> iter;
        private final boolean inverse;
        private final Resource subject;
        private final IRI predicate;
        private final Value object;
        // TODO: use contexts?
        private final Resource[] contexts;

        private Statement nextStatement;

        public SolutionIteration(CloseableIteration<RippleList, RippleException> iter,
                                 boolean inverse,
                                 Resource subject,
                                 IRI predicate,
                                 Value object,
                                 Resource... contexts) throws SailException {
            this.iter = iter;
            this.inverse = inverse;
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
            this.contexts = contexts;

            advanceToNext();
        }


        @Override
        public void close() throws SailException {
            try {
                iter.close();
            } catch (RippleException e) {
                throw new SailException(e);
            }
        }

        @Override
        public boolean hasNext() throws SailException {
            return null != nextStatement;
        }

        private void advanceToNext() throws SailException {
            try {
                nextStatement = null;
                if (iter.hasNext()) {
                    RippleList stack = iter.next();

                    if (inverse) {
                        Value subj = modelConnection.toRDF(stack.getFirst());
                        RippleSesameValue s = (RippleSesameValue) valueFactory.nativize(subj);
                        s.setStack(stack);

                        nextStatement = valueFactory.createStatement((Resource) s, predicate, object);
                        //System.out.println("nextStatement(1): " + nextStatement);
                    } else {
                        Value r = modelConnection.toRDF(stack.getFirst());
                        Value obj;
                        RippleSesameValue o;
                        if (null == r) {
                            //System.out.println("stack = " + stack);
                            //System.out.println("subject = " + subject);
                            //System.out.println("predicate = " + predicate);
                            //System.out.println("object = " + object);

                            o = new RippleBNode();
                            //continue;
                            o.setStack(stack);
                        } else {
                            obj = r;
                            o = (RippleSesameValue) valueFactory.nativize(obj);
                            o.setStack(stack);
                        }

                        nextStatement = valueFactory.createStatement(subject, predicate, (Value) o);
                        //System.out.println("nextStatement(2): " + nextStatement);
                    }
                }
            } catch (RippleException e) {
                throw new SailException(e);
            }
        }

        @Override
        public Statement next
                () throws SailException {
            Statement s = nextStatement;

            advanceToNext();

            return s;
        }

        @Override
        public void remove() throws SailException {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void begin() {
        // Do nothing; adapt to SesameModel, which begins transactions implicitly
    }
}
