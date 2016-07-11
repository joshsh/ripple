package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RDFImporter;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;

import java.io.InputStream;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleListTest extends RippleTestCase {
    @Test
    public void testListRDFEquivalence() throws Exception {
        assertReducesTo("(1 2 3) rdf:rest.", "(2 3)");
        assertReducesTo("(1 2 3) rdf:rest. rdf:first.", "2");

        /* TODO: in order to pass, these test cases will require a new feature.
        assertReducesTo( "rdf:nil rdf:type >>", "rdf:List" );
        assertReducesTo( "() rdf:type >>", "rdf:List" );
        assertReducesTo( "(1 2 3) rdf:type >>", "rdf:List" );
        */
    }

    @Test
    public void testFromRDF() throws Exception {
        final ModelConnection mc = getTestModel().createConnection();

        InputStream is = RippleListTest.class.getResourceAsStream("listTest.ttl");
        RDFImporter importer = new RDFImporter(mc);
        SesameInputAdapter.parse(is, importer, "", RDFFormat.TURTLE);
        mc.commit();
        is.close();

        Value head;
        Collector<RippleList> created = new Collector<>();
        final Collector<RippleList> allowed = new Collector<>();

        Sink<RippleList> verifySink = list -> {
            boolean found = false;

            for (Iterator<RippleList> iter = allowed.iterator(); iter.hasNext(); ) {
                if (0 == mc.getComparator().compare(iter.next(), list)) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);
        };

        Value l1 = mc.valueOf("1", XMLSchema.STRING);
        Value l2 = mc.valueOf("2", XMLSchema.STRING);
        Value l1a = mc.valueOf("1a", XMLSchema.STRING);
        Value l1b = mc.valueOf("1b", XMLSchema.STRING);
        Value l2a = mc.valueOf("2a", XMLSchema.STRING);
        Value l2b = mc.valueOf("2b", XMLSchema.STRING);

        head = createIRI("urn:test.RippleListTest.FromRdfTest#simpleList", mc);
        created.clear();
        mc.toList(head, created);
        assertEquals(1, created.size());
        allowed.clear();
        allowed.accept(mc.list().push(l2).push(l1));
        created.writeTo(verifySink);

        head = createIRI("urn:test.RippleListTest.FromRdfTest#firstBranchingList", mc);
        created.clear();
        mc.toList(head, created);
        assertEquals(2, created.size());
        allowed.clear();
        allowed.accept(mc.list().push(l2).push(l1a));
        allowed.accept(mc.list().push(l2).push(l1b));
        created.writeTo(verifySink);

        head = createIRI("urn:test.RippleListTest.FromRdfTest#restBranchingList", mc);
        created.clear();
        mc.toList(head, created);
        assertEquals(2, created.size());
        allowed.clear();
        allowed.accept(mc.list().push(l2a).push(l1));
        allowed.accept(mc.list().push(l2b).push(l1));
        created.writeTo(verifySink);

        head = createIRI("urn:test.RippleListTest.FromRdfTest#firstAndRestBranchingList", mc);
        created.clear();
        mc.toList(head, created);
        assertEquals(4, created.size());
        allowed.clear();
        allowed.accept(mc.list().push(l2a).push(l1a));
        allowed.accept(mc.list().push(l2a).push(l1b));
        allowed.accept(mc.list().push(l2b).push(l1a));
        allowed.accept(mc.list().push(l2b).push(l1b));
        created.writeTo(verifySink);

        // Note: the circular list is not tested.

        mc.close();
    }

    @Test
    public void testListConcatenation() throws Exception {
        ModelConnection mc = getTestModel().createConnection();
        Object
                a0 = 42,
                a1 = 137,
                a2 = 23,
                b0 = 216;
        mc.close();

        RippleList
                listA = mc.list().push(a2).push(a1).push(a0),
                listB = mc.list().push(b0);

        assertEquals(listA.length(), 3);
        assertEquals(listA.get(0), a0);
        assertEquals(listA.get(1), a1);
        assertEquals(listA.get(2), a2);
        assertEquals(listB.length(), 1);
        assertEquals(listB.get(0), b0);
        assertEquals(mc.list().length(), 0);

        RippleList lresult;

        lresult = listA.concat(listB);
        assertEquals(lresult.length(), 4);
        assertEquals(lresult.get(0), a0);
        assertEquals(lresult.get(1), a1);
        assertEquals(lresult.get(2), a2);
        assertEquals(lresult.get(3), b0);
        lresult = listB.concat(listA);
        assertEquals(lresult.length(), 4);
        assertEquals(lresult.get(0), b0);
        assertEquals(lresult.get(1), a0);
        assertEquals(lresult.get(2), a1);
        assertEquals(lresult.get(3), a2);

        lresult = listA.concat(mc.list());
        assertEquals(lresult.length(), 3);
        assertEquals(lresult.get(0), a0);
        assertEquals(lresult.get(1), a1);
        assertEquals(lresult.get(2), a2);

        lresult = mc.list().concat(listB);
        assertEquals(lresult.length(), 1);
        assertEquals(lresult.get(0), b0);

        lresult = mc.list().concat(mc.list());
        assertEquals(lresult.length(), 0);
        assertRippleEquals(lresult, mc.list());
    }
}

