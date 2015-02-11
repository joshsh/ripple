package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.linkeddata.RDFUtils;
import net.fortytwo.linkeddata.RedirectManager;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.sail.SailException;
import org.restlet.data.MediaType;
import org.restlet.representation.StreamRepresentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPRepresentation extends StreamRepresentation {
    private InputStream inputStream;
    private HttpUriRequest method;

    // from HttpComponents docs: "the HttpClient instance and connection manager should be shared
    // among all threads for maximum efficiency"
    private static final HttpClient client;

    static {
        try {
            client = HTTPUtils.createClient(false);
        } catch (RippleException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // Note: the URI is immediately dereferenced
    public HTTPRepresentation(final String uri, final RedirectManager redirects, final String acceptHeader)
            throws RippleException {

        super(null);

        URL getUrl;
        try {
            getUrl = RDFUtils.iriToUrl(uri);
        } catch (MalformedURLException e) {
            throw new RippleException(e);
        }

        HttpResponse response;
        String redirectUrl = null;

        try {
            while (true) {
                method = HTTPUtils.createGetMethod(getUrl.toString());
                HTTPUtils.setAcceptHeader(method, acceptHeader);

                /*
                the amount of time, in milliseconds, which was spent on a courtesy delay
                (to avoid overloading remote servers) while creating this representation, as opposed
                to time spent waiting for a response from the remote server or receiving packet data.
                This is potentially important for response time analysis.
                 */
                long idleTime = HTTPUtils.throttleHttpRequest(method);

                try {
                    response = client.execute(method);
                } catch (IOException e) {
                    throw new RippleException(e);
                }

                int code = response.getStatusLine().getStatusCode();
                int c = code / 100;
                if (2 == c) {
                    break;
                } else if (3 == c) {
                    redirectUrl = response.getFirstHeader("Location").getValue();

                    try {
                        // do not repeatedly retrieve the same document
                        if (redirects.isExistingRedirectTo(redirectUrl)) {
                            throw new RedirectToExistingDocumentException();
                        }

                        method.abort();
                    } catch (SailException e) {
                        throw new RippleException(e);
                    }

                    try {
                        getUrl = new URL(redirectUrl);
                    } catch (MalformedURLException e) {
                        throw new RippleException(e);
                    }
                } else {
                    throw new ErrorResponseException("" + code + " response for resource <"
                            + StringUtils.escapeURIString(uri) + ">");
                }
            }

            // keep the connection open only if we did not exit abnormally
            method = null;
        } finally {
            // if we followed one or more redirects, record the redirection to save on future work
            if (null != redirectUrl) {
                try {
                    redirects.persistRedirect(uri, redirectUrl);
                } catch (SailException e) {
                    throw new RippleException(e);
                }
            }

            if (method != null) {
                method.abort();
                method = null;
            }
        }

        InputStream is;

        try {
            is = response.getEntity().getContent();
        } catch (IOException e) {
            throw new RippleException(e);
        }

        inputStream = new HttpRepresentationInputStream(is);

        Header h = response.getFirstHeader(HTTPUtils.CONTENT_TYPE);
        if (null == h) {
            throw new InvalidResponseException("no content-type header served for resource <"
                    + StringUtils.escapeURIString(uri) + ">");
        }

        String mtStr = h.getValue().split(";")[0];
        if (null == mtStr || 0 == mtStr.length()) {
            throw new InvalidResponseException("no media type found for resource <"
                    + StringUtils.escapeURIString(uri) + ">");
        }
        MediaType mt = new MediaType(mtStr);
//System.out.println( "discovered media type is: " + mt );
        setMediaType(mt);
    }

    public ReadableByteChannel getChannel() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public InputStream getStream() throws IOException {
        return inputStream;
    }

    public void write(final OutputStream outputStream) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void write(final WritableByteChannel writableByteChannel) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * A helper InputStream which closes the HTTP connection when it itself is
     * closed
     */
    private class HttpRepresentationInputStream extends InputStream {
        private InputStream innerInputStream;

        public HttpRepresentationInputStream(final InputStream is) {
            innerInputStream = is;
        }

        public int read() throws IOException {
            return innerInputStream.read();
        }

        @Override
        public void close() throws IOException {
            if (null != method) {
                method.abort();
            }

            innerInputStream.close();
        }

        @Override
        public int available() throws IOException {
            return innerInputStream.available();
        }
    }

    public class ErrorResponseException extends RippleException {
        public ErrorResponseException(final String message) {
            super(message);
        }
    }

    public class InvalidResponseException extends RippleException {
        public InvalidResponseException(final String message) {
            super(message);
        }
    }

    public class RedirectToExistingDocumentException extends RippleException {
        public RedirectToExistingDocumentException() {
            super();
        }
    }
}
