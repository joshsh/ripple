package net.fortytwo.linkeddata;

import net.fortytwo.flow.rdf.SesameOutputAdapter;
import net.fortytwo.flow.rdf.diff.RDFDiffContextFilter;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.flow.rdf.diff.RDFDiffSource;
import net.fortytwo.flow.AdapterSink;
import net.fortytwo.flow.rdf.diff.AdapterRDFDiffSink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.util.HTTPUtils;
import net.fortytwo.ripple.util.RDFUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * Note: this class is not thread-safe.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SparqlUpdater<E extends Exception> {
    private final RDFDiffContextFilter<RDFHandlerException> contextFilter;
    private final RDFDiffSink<E> sink;
    private final URIMap uriMap;
    private final AdapterSink.ExceptionAdapter<RippleException> exAdapter;
    private final AdapterSink.ExceptionAdapter<RDFHandlerException> exAdapter2;

    public SparqlUpdater(final URIMap uriMap, final RDFDiffSink<E> sink) {
        this.uriMap = uriMap;
        this.sink = sink;

        contextFilter = new RDFDiffContextFilter<RDFHandlerException>();

        exAdapter = new AdapterSink.ExceptionAdapter<RippleException>() {
            public void doThrow(Exception e) throws RippleException {
                throw new RippleException(e);
            }
        };

        exAdapter2 = new AdapterSink.ExceptionAdapter<RDFHandlerException>() {
            public void doThrow(Exception e) throws RDFHandlerException {
                throw new RDFHandlerException(e);
            }
        };
    }

    public RDFDiffSink<RippleException> getSink() {
        return new AdapterRDFDiffSink<RDFHandlerException, RippleException>(contextFilter, exAdapter);
    }

    public void flush() throws RDFHandlerException, RippleException {
        Iterator<Resource> contexts = contextFilter.contextIterator();
        while (contexts.hasNext()) {
            Resource context = contexts.next();
            RDFDiffSource<RDFHandlerException> source = contextFilter.sourceForContext(context);

            // Some statements cannot be written to the Semantic Web.
            if (null != context
                    && context instanceof URI
                    && RDFUtils.isHttpUri((URI) context)) {
                String url = uriMap.get(context.toString());

                postUpdate(url, source);
            }

// The statements written to the triple store should depend on the outcome of
// the update operation (if any).
            RDFDiffSink<RDFHandlerException> newSink = new AdapterRDFDiffSink<E, RDFHandlerException>(sink, exAdapter2);
            source.writeTo(newSink);
        }

        contextFilter.clear();
    }

    private void postUpdate(final String url, final RDFDiffSource<RDFHandlerException> source)
            throws RDFHandlerException, RippleException {
        String postData = createPostData(source);
        System.out.println("posting update to url <" + url + ">: " + postData);

        PostMethod method = HTTPUtils.createSparqlUpdateMethod(url);
        NameValuePair[] data = {   // FIXME: is this correct?
                new NameValuePair(HTTPUtils.BODY, postData)
        };
        method.setRequestBody(data);
        HTTPUtils.registerMethod(method);

        HttpClient client = HTTPUtils.createClient();

        int responseCode;

        try {
            client.executeMethod(method);

            // ...do something with the response..

            responseCode = method.getStatusCode();
            method.releaseConnection();
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        System.out.println("response code = " + responseCode);
    }

    private String createPostData(final RDFDiffSource<RDFHandlerException> source) throws RDFHandlerException, RippleException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);

        SesameOutputAdapter adapter
                = RDFUtils.createOutputAdapter(bos, RDFFormat.TURTLE);

        ps.println("INSERT {");
        adapter.startRDF();
        source.adderSource().namespaceSource().writeTo(adapter.namespaceSink());
        source.adderSource().statementSource().writeTo(adapter.statementSink());
        adapter.endRDF();
        ps.println("}");

        // Note: since some statements are rejected, we will sometimes end up
        // with an empty DELETE analysis.
        ps.println("DELETE {");
        adapter.startRDF();
// TODO: ignore statements with blank nodes as subject or object... UNLESS they're found to serve some purpose
        source.subtractorSource().namespaceSource().writeTo(adapter.namespaceSink());
        source.subtractorSource().statementSource().writeTo(adapter.statementSink());
        adapter.endRDF();
        ps.println("}");

        return bos.toString();
    }
}

