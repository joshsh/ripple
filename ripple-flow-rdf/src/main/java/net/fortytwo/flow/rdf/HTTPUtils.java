package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.openrdf.rio.RDFFormat;

import java.util.HashMap;
import java.util.Map;

//To consider at some point: caching, authorization

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPUtils {
    public static final String
            ACCEPT = "Accept",
            BODY = "Body",
            CONTENT_TYPE = "Content-Type",
            SPARQL_QUERY = "application/sparql-query",
            USER_AGENT = "User-Agent";

    private static final Map<String, Long> lastRequestByHost = new HashMap<String, Long>();

    private static final long COURTESY_INTERVAL;
    private static final long CONNECTION_TIMEOUT;

    static {
        try {
            COURTESY_INTERVAL = Ripple.getConfiguration().getLong(
                    Ripple.HTTPCONNECTION_COURTESY_INTERVAL);
            CONNECTION_TIMEOUT = Ripple.getConfiguration().getLong(
                    Ripple.HTTPCONNECTION_TIMEOUT);
        } catch (RippleException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static HttpClient createClient(final boolean autoRedirect) throws RippleException {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout((int) CONNECTION_TIMEOUT)
                .setConnectTimeout((int) CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout((int) CONNECTION_TIMEOUT)
                        //.setStaleConnectionCheckEnabled(true)
                .build();

        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                        //.disableAutomaticRetries()
                        //.disableConnectionState()
                .disableContentCompression()  // automatic compression can derail content negotiation
                //.disableRedirectHandling()
                //.useSystemProperties()
                ;
        if (!autoRedirect) {
            // redirect manually, keeping track of eventually retrieved documents
            builder = builder.disableRedirectHandling();
        }

        return builder.build();
    }

    public static HttpGet createGetMethod(final String url) throws RippleException {

        HttpGet method;

        try {
            method = new HttpGet(url);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        setAgent(method);

        return method;
    }

    public static HttpPost createPostMethod(final String url) throws RippleException {
        HttpPost method;

        try {
            method = new HttpPost(url);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        setAgent(method);

        return method;
    }

    public static HttpGet createRdfGetMethod(final String url) throws RippleException {
        HttpGet method = createGetMethod(url);

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (RDFFormat f : RDFFormat.values()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(f.getDefaultMIMEType());
        }

        setAcceptHeader(method, sb.toString());

        return method;
    }

    public static HttpPost createSparqlUpdateMethod(final String url) throws RippleException {
        HttpPost method = createPostMethod(url);
        setContentTypeHeader(method, SPARQL_QUERY);
        return method;
    }

    public static void setContentTypeHeader(final HttpRequest method, final String value)
            throws RippleException {
        try {
            method.setHeader(CONTENT_TYPE, value);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    public static void setAcceptHeader(final HttpRequest method, final String value)
            throws RippleException {
        try {
            method.setHeader(ACCEPT, value);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    public static void setAcceptHeader(final HttpRequest method, final String[] mimeTypes)
            throws RippleException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mimeTypes.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(mimeTypes[i]);
        }

        setAcceptHeader(method, sb.toString());
    }

    /**
     * Enforces crawler etiquette with respect to timing of HTTP requests.
     * That is, it avoids the Ripple client making a nuisance of itself by
     * making too many requests, too quickly, of the same host.
     * TODO: request and respect robots.txt, if present.
     *
     * @return the amount of time, in milliseconds, that is spent idling for the sake of crawler etiquette
     */
    public static long throttleHttpRequest(final HttpRequest method) throws RippleException {
        String uri = method.getRequestLine().getUri();

        // Some connections (e.g. file system operations) have no host.  Don't
        // bother regulating them.
        if (null == uri || 0 == uri.length() || !(uri.startsWith("http://") || uri.startsWith("https://"))) {
            return 0;
        }

        String host = uri.substring(uri.indexOf("//") + 2);
        int i = host.indexOf("/");
        if (i > 0) {
            host = host.substring(0, i);
        }
        i = host.indexOf("#");
        if (i > 0) {
            host = host.substring(0, i);
        }

        long now = System.currentTimeMillis();

        Long lastRequest;
        long w = 0;

        synchronized (lastRequestByHost) {
            lastRequest = lastRequestByHost.get(host);

            // We've already made a request of this host.
            if (null != lastRequest) {
                // If it hasn't been long enough since the last request from the same
                // host, wait a bit before issuing a new request.
                if (now - lastRequest < COURTESY_INTERVAL) {
                    w = lastRequest + COURTESY_INTERVAL - now;
                }
            }

            // Record the projected start time of the request beforehand, to
            // avoid any other requests being scheduled without knowledge of
            // this one.
            lastRequestByHost.put(host, w + now);
        }

        // Wait if necessary.
        if (w > 0) {
            try {
                Thread.sleep(w);
            } catch (InterruptedException e) {
                throw new RippleException(e);
            }
        }

        return System.currentTimeMillis() - now;
    }

    private static void setAgent(final HttpRequest method) {
        method.setHeader(USER_AGENT, Ripple.getName() + "/" + Ripple.getVersion());
    }

    public static void main(final String[] args) throws RippleException {
        HttpClient client = createClient(true);
        //System.out.println("done");
    }
}

