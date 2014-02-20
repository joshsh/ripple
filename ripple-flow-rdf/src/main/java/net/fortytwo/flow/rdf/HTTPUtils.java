package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.openrdf.rio.RDFFormat;

import java.util.Date;
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

    private static final Map<String, Date> LAST_REQUEST_BY_HOST = new HashMap<String, Date>();

    private static long courtesyInterval;
    private static long connectionTimeout;
    private static boolean initialized = false;

    private static void initialize() throws RippleException {
        courtesyInterval = Ripple.getConfiguration().getLong(
                Ripple.HTTPCONNECTION_COURTESY_INTERVAL);
        connectionTimeout = Ripple.getConfiguration().getLong(
                Ripple.HTTPCONNECTION_TIMEOUT);
        initialized = true;
    }

    public static HttpClient createClient() throws RippleException {
        if (!initialized) {
            initialize();
        }

        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
//        client.getParams().setConnectionManagerTimeout( Ripple.httpConnectionTimeout() );
        client.getParams().setParameter("http.connection.timeout", (int) connectionTimeout);
        client.getParams().setParameter("http.socket.timeout", (int) connectionTimeout);
        return client;
    }

    public static HttpMethod createGetMethod(final String url) throws RippleException {
        HttpMethod method;

        try {
            method = new GetMethod(url);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        setAgent(method);

        return method;
    }

    public static PostMethod createPostMethod(final String url) throws RippleException {
        PostMethod method;

        try {
            method = new PostMethod(url);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        setAgent(method);

        return method;
    }

    ////////////////////////////////////////////////////////////////////////////

    public static HttpMethod createRdfGetMethod(final String url) throws RippleException {
        HttpMethod method = createGetMethod(url);

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

    public static PostMethod createSparqlUpdateMethod(final String url) throws RippleException {
        PostMethod method = createPostMethod(url);
        setContentTypeHeader(method, SPARQL_QUERY);
        return method;
    }

    ////////////////////////////////////////////////////////////////////////////

    public static void setContentTypeHeader(final HttpMethod method, final String value)
            throws RippleException {
        try {
            method.setRequestHeader(CONTENT_TYPE, value);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    public static void setAcceptHeader(final HttpMethod method, final String value)
            throws RippleException {
        try {
            method.setRequestHeader(ACCEPT, value);
        } catch (Throwable t) {
            throw new RippleException(t);
        }
    }

    public static void setAcceptHeader(final HttpMethod method, final String[] mimeTypes)
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

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Enforces crawler etiquette with respect to timing of HTTP requests.
     * That is, it avoids the Ripple client making a nuisance of itself by
     * making too many requests, too quickly, of the same host.
     * TODO: request and respect robots.txt, if present.
     *
     * @return the amount of time, in milliseconds, that is spent idling for the sake of crawler etiquette
     */
    public static long throttleHttpRequest(final HttpMethod method) throws RippleException {
        if (!initialized) {
            initialize();
        }

        String host;

        try {
            host = method.getURI().getHost();
        } catch (URIException e) {
            throw new RippleException(e);
        }

        // Some connections (e.g. file system operations) have no host.  Don't
        // bother regulating them.
        if (null != host && host.length() > 0) {
            Date now = new Date();
            long delay = courtesyInterval;

            Date lastRequest;
            long w = 0;

            synchronized (LAST_REQUEST_BY_HOST) {
                lastRequest = LAST_REQUEST_BY_HOST.get(host);

                // We've already made a request of this host.
                if (null != lastRequest) {
                    // If it hasn't been long enough since the last request from the same
                    // host, wait a bit before issuing a new request.
                    if (now.getTime() - lastRequest.getTime() < delay) {
                        w = lastRequest.getTime() + delay - now.getTime();
                    }
                }

                // Record the projected start time of the request beforehand, to
                // avoid any other requests being scheduled without knowledge of
                // this one.
                LAST_REQUEST_BY_HOST.put(host, new Date(w + now.getTime()));
            }

            // Wait if necessary.
            if (w > 0) {
                try {
//LOGGER.info( "    waiting " + w + " milliseconds" );
                    Thread.sleep(w);
                } catch (InterruptedException e) {
                    throw new RippleException(e);
                }
            }

            return System.currentTimeMillis() - now.getTime();
        } else {
            return 0;
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private static void setAgent(final HttpMethod method) {
        method.setRequestHeader(USER_AGENT, Ripple.getName() + "/" + Ripple.getVersion());
    }
}

