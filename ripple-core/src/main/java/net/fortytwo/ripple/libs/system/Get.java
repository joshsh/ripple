package net.fortytwo.ripple.libs.system;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.openrdf.model.vocabulary.XMLSchema;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A primitive which consumes an information resource, issues a GET request for
 * the resource, then produces the retrieved data as a string.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Get extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            SystemLibrary.NS_2013_03 + "get",
            SystemLibrary.NS_2008_08 + "get",
            SystemLibrary.NS_2007_08 + "get",
            SystemLibrary.NS_2007_05 + "get"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("uri", null, true)};
    }

    public String getComment() {
        return "issues a GET request and produces the received data as a string";
    }

    public Get()
            throws RippleException {
        super();
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;

        String result;

        String uriStr = stack.getFirst().toString();
        //uriStr = mc.getModel().getURIMap().get( uriStr );
        stack = stack.getRest();

        HttpGet method = HTTPUtils.createGetMethod(uriStr);
        HTTPUtils.throttleHttpRequest(method);

        HttpClient client = HTTPUtils.createClient(true);

        InputStream body;

        HttpResponse response;

        try {
            response = client.execute(method);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        int code = response.getStatusLine().getStatusCode();
        if (2 != code / 100) {
            throw new RippleException("response code " + code + " for resource at " + uriStr);
        }

        try {
            body = response.getEntity().getContent();
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(body));
            StringBuffer sb = new StringBuffer();
            String nextLine = "";
            boolean first = true;
            while ((nextLine = br.readLine()) != null) {
                if (first) {
                    first = false;
                } else {
                    sb.append('\n');
                }

                sb.append(nextLine);
            }
            result = sb.toString();

            body.close();
        } catch (java.io.IOException e) {
            throw new RippleException(e);
        }

        try {
            method.abort();
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        solutions.accept(stack.push(mc.valueOf(result, XMLSchema.STRING)));
    }
}

