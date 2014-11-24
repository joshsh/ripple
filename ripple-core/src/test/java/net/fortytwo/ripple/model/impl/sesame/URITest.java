package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RDFImporter;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.rio.RDFFormat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URITest extends RippleTestCase {
    private static final String URI_NS = "http://id.ninebynine.org/wip/2004/uritest/";

    private static final URI
            COMMENT = RDFS.COMMENT,
            LABEL = RDFS.LABEL,
            TYPE = RDF.TYPE,
            BASE = new URIImpl(URI_NS + "base"),
            FRAG = new URIImpl(URI_NS + "frag"),
            PATH = new URIImpl(URI_NS + "path"),
            PORT = new URIImpl(URI_NS + "port"),
            QUERY = new URIImpl(URI_NS + "query"),
            REG = new URIImpl(URI_NS + "reg"),
            SCHEME = new URIImpl(URI_NS + "scheme"),
            TEST = new URIImpl(URI_NS + "test"),
            URITEST = new URIImpl(URI_NS + "UriTest"),
            USER = new URIImpl(URI_NS + "user");

    private enum TestType {
        ABSID("AbsId"),
        ABSRF("AbsRf"),
        ABS2REL("Abs2Rel"),
        DECOMP("Decomp"),
        NORMCASE("NormCase"),
        NORMESC("NormEsc"),
        NORMPATH("NormPath"),
        INVRF("InvRf"),
        RELATIVE("Relative"),
        RELRF("RelRf"),
        REL2ABS("Rel2Abs");

        String name;

        TestType(final String n) {
            name = n;
        }

        public String getName() {
            return name;
        }

        public static TestType find(final String name)
                throws RippleException {
            for (TestType type : TestType.values())
                if (type.name.equals(name))
                    return type;
            throw new RippleException("no such TestType: " + name);
        }
    }

    private static String strVal(Object subj, Object pred, ModelConnectionHelper h, final ModelConnection mc)
            throws Exception {
        Object obj = h.findSingleObject(subj, pred);

        if (null == obj) {
            return null;
        } else {
            return mc.toRDF(obj).stringValue();
        }
    }

    private class UriTestCase {
        public TestType type;

        public String
                base,
                comment,
                frag,
                label,
                path,
                port,
                query,
                reg,
                scheme,
                user;

        public UriTestCase(final Object r, final ModelConnection mc)
                throws Exception {
            ModelConnectionHelper h = new ModelConnectionHelper(mc);

            base = strVal(r, BASE, h, mc);
            comment = strVal(r, COMMENT, h, mc);
            frag = strVal(r, FRAG, h, mc);
            label = strVal(r, LABEL, h, mc);
            path = strVal(r, PATH, h, mc);
            port = strVal(r, PORT, h, mc);
            query = strVal(r, QUERY, h, mc);
            reg = strVal(r, REG, h, mc);
            scheme = strVal(r, SCHEME, h, mc);
            user = strVal(r, USER, h, mc);

            System.out.println("r = " + r);
            type = TestType.find(
                    ((URI) mc.toRDF(h.findSingleObject(r, TEST))).getLocalName());
            System.out.println("    type = " + type);
        }

        public void test(final ModelConnection mc)
                throws Exception {
            String fakeBase = "http://example.org/";
            URI uri;

            switch (type) {
//              case ABSID:
                case ABSRF:
                    assertFalse(null == base);
                    uri = createURI(base, mc);
                    break;
//              case ABS2REL:
//              case DECOMP:
//              case INVRF:  // ?
//              case NORMCASE:
//              case NORMESC:
//              case NORMPATH:
//              case RELATIVE:
                case RELRF:
                    assertFalse(null == base);
                    //uri = createURI(fakeBase + base, mc);
                    break;
//              case REL2ABS:
                default:
                    System.out.println("unhandled test case!");
            }
        }
    }

    public void testValueOf() throws Exception {
        String s = "http://example.org/foo";
        java.net.URI u = java.net.URI.create(s);
        Value v = modelConnection.valueOf(u);
        assertEquals(s, v.stringValue());
    }

    public void testGrahamKlyneCases() throws Exception {
        ModelConnection mc = getTestModel().createConnection();

        // See: http://lists.w3.org/Archives/Public/uri/2006Feb/0003.html
        InputStream is = URITest.class.getResourceAsStream("UriTest.n3");

        RDFImporter importer = new RDFImporter(mc);
        SesameInputAdapter.parse(is, importer, "", RDFFormat.N3);
        mc.commit();

        Collector<Object> cases = new Collector<>();
        StatementPatternQuery query = new StatementPatternQuery(null, TYPE, URITEST);
        mc.query(query, cases, false);

        for (Object aCase : cases) {
            Object caseValue = aCase;
            (new UriTestCase(caseValue, mc)).test(mc);
        }

        is.close();
        mc.close();
    }

    private void namespaceTest(final String uri,
                               final String ns,
                               final String localName,
                               final ModelConnection mc)
            throws Exception {
        URI uriCreated = createURI(uri, mc);
        String nsCreated = uriCreated.getNamespace();
        String localNameCreated = uriCreated.getLocalName();

        assertEquals(uriCreated.toString(), uri);
        assertEquals(nsCreated, ns);
        assertEquals(localNameCreated, localName);
    }

    public void testURINamespace() throws Exception {
        ModelConnection mc = getTestModel().createConnection();

        InputStream is = URITest.class.getResourceAsStream("UriNamespaceTest.txt");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        int lineno = 0;

        // Break out when end of stream is reached.
        while (true) {
            String line = reader.readLine();
            lineno++;

            if (null == line)
                break;

            line = line.trim();

            if (!line.startsWith("#") && !line.equals("")) {
                String[] args = line.split("\t");
                if (args.length != 3)
                    throw new RippleException("wrong number of aguments on line " + lineno);
                namespaceTest(args[0], args[1], args[2], mc);
            }
        }

        is.close();
        mc.close();
    }
}

