package net.fortytwo.ripple.sail;

import info.aduna.iteration.CloseableIteration;
import junit.framework.TestCase;
import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.StackEvaluator;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

import java.util.HashSet;
import java.util.Set;

/**
 * User: josh
 * Date: 6/2/11
 * Time: 2:49 PM
 */
public class RippleSailTest extends TestCase {

    public void testAll() throws Exception {
        Sail baseSail = new MemoryStore();
        baseSail.initialize();
        RippleSail sail = new RippleSail(baseSail);
        sail.initialize();
        try {
            ValueFactory vf = sail.getValueFactory();
            URI foo = vf.createURI("http://example.org/foo");

            addDummyData(baseSail);

            SailConnection sc = sail.getConnection();
            try {
                Set<Statement> set;

                set = toSet(sc.getStatements(foo, RDF.FIRST, null, false));
                for (Statement st : set) {
                    System.out.println(st);
                }

                set = toSet(sc.getStatements(foo, sail.getValueFactory().createURI("http://fortytwo.net/2011/04/ripple/control#apply"), null, false));
                for (Statement st : set) {
                    System.out.println(st);
                }
            } finally {
                sc.close();
            }
        } finally {
            sail.shutDown();
        }
    }

    public void testNothing() throws Exception {
        // This is necessary to avoid race conditions.
        Ripple.enableAsynchronousQueries(false);

        Sail sail = new MemoryStore();
        sail.initialize();
        addDummyData(sail);

        Model model = new SesameModel(sail);
        StackEvaluator eval = new LazyStackEvaluator();

        ModelConnection mc = model.createConnection();
        try {
            Collector<StackContext, RippleException> solutions = new Collector<StackContext, RippleException>();
            //*
            RippleList stack = mc.list().push(mc.canonicalValue(new RDFValue(new URIImpl("http://example.org/foo"))))
                    .push(mc.canonicalValue(new RDFValue(RDF.FIRST))).push(Operator.OP);
            //*/
            /*
            RippleList stack = mc.list().push(mc.numericValue(2)).push(mc.numericValue(3))
                    .push(mc.canonicalValue(new RDFValue(new URIImpl("http://fortytwo.net/2011/04/ripple/math#add")))).push(Operator.OP);
            //*/
            System.out.println("asynch: " + Ripple.asynchronousQueries());
            eval.apply(new StackContext(stack, mc), solutions);
            System.out.println("solutions.size() = " + solutions.size());
            for (StackContext c : solutions) {
                System.out.println("solution: " + c.getStack());
            }
        } finally {
            mc.close();
        }

        model.shutDown();
        sail.shutDown();
    }

    private void addDummyData(final Sail sail) throws Exception {
        Repository repo = new SailRepository(sail);
        RepositoryConnection rc = repo.getConnection();
        try {
            rc.add(RippleSail.class.getResource("rippleSailTest.trig"), "", RDFFormat.TRIG);
            rc.commit();
        } finally {
            rc.close();
        }
    }

    private Set<Statement> toSet(final CloseableIteration<? extends Statement, SailException> i) throws SailException {
        try {
            Set<Statement> set = new HashSet<Statement>();
            while (i.hasNext()) {
                set.add(i.next());
            }
            return set;
        } finally {
            i.close();
        }
    }
}
