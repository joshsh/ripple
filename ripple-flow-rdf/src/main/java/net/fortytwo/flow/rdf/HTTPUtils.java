package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

//To consider at some point: caching, authorization

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPUtils {
    public static final String USER_AGENT = "User-Agent";

    private static final Map<String, Long> lastRequestByHost = new HashMap<>();

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

        ignoreSSLErrors(builder);

        return builder.build();
    }

    // Set up a Trust Strategy that allows all certificates
    // See: http://literatejava.com/networks/ignore-ssl-certificate-errors-apache-httpclient-4-4/
    private static void ignoreSSLErrors(final HttpClientBuilder builder) throws RippleException {
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RippleException(e);
        }
        builder.setSslcontext(sslContext);

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        builder.setConnectionManager(connMgr);
    }

    public static HttpGet createGetMethod(final String url) {

        HttpGet method;

        method = new HttpGet(url);

        setAgent(method);

        return method;
    }

    /**
     * Enforces crawler etiquette with respect to timing of HTTP requests.
     * That is, it avoids the Ripple client making a nuisance of itself by
     * making too many requests, too quickly, of the same host.
     * TODO: request and respect robots.txt, if present.
     */
    public static void throttleHttpRequest(final HttpRequest method) throws RippleException {
        String uri = method.getRequestLine().getUri();

        // Some connections (e.g. file system operations) have no host.  Don't
        // bother regulating them.
        if (null == uri || 0 == uri.length() || !(uri.startsWith("http://") || uri.startsWith("https://"))) {
            return;
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
    }

    private static void setAgent(final HttpRequest method) {
        method.setHeader(USER_AGENT, Ripple.getName() + "/" + Ripple.getVersion());
    }
}

