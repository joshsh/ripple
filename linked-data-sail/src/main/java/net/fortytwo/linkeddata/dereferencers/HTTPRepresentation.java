package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.linkeddata.RedirectManager;
import net.fortytwo.linkeddata.util.HTTPUtils;
import net.fortytwo.linkeddata.util.RDFUtils;
import net.fortytwo.linkeddata.util.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPRepresentation extends StreamRepresentation {
    private static final Logger logger = Logger.getLogger(HTTPRepresentation.class.getName());

    private final InputStream inputStream;
    private HttpUriRequest method;

    // from HttpComponents docs: "the HttpClient instance and connection manager should be shared
    // among all threads for maximum efficiency"
    private static final HttpClient client;

    static {
        try {
            client = HTTPUtils.createClient(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed to initialize", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public HTTPRepresentation(final String iri, final RedirectManager redirects, final String acceptHeader)
            throws IOException {
        super(null);

        // the IRI is immediately dereferenced
        HttpResponse response = dereference(iri, redirects, acceptHeader);

        inputStream = new HttpRepresentationInputStream(response.getEntity().getContent());

        setMediaType(findMediaType(iri, response));
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

    private MediaType findMediaType(final String iri, final HttpResponse response) throws InvalidResponseException {
        Header contentTypeHeader = response.getFirstHeader(HTTPUtils.CONTENT_TYPE);
        if (null == contentTypeHeader) {
            throw new InvalidResponseException("no content-type header served for resource <"
                    + StringUtils.escapeURIString(iri) + ">");
        }

        String mtStr = contentTypeHeader.getValue().split(";")[0];
        if (null == mtStr || 0 == mtStr.length()) {
            throw new InvalidResponseException("no media type found for resource <"
                    + StringUtils.escapeURIString(iri) + ">");
        }
        return new MediaType(mtStr);
    }

    private HttpResponse dereference(final String iri, final RedirectManager redirects, final String acceptHeader) throws IOException {
        URL getUrl = RDFUtils.iriToUrl(iri);
        HttpResponse response;
        String redirectUrl = null;

        while (true) {
            method = HTTPUtils.createGetMethod(getUrl.toString());
            HTTPUtils.setAcceptHeader(method, acceptHeader);

            HTTPUtils.throttleHttpRequest(method);

            response = client.execute(method);
            int code = response.getStatusLine().getStatusCode();
            int c = code / 100;
            if (2 == c) {
                break;
            } else if (3 == c) {
                redirectUrl = response.getFirstHeader("Location").getValue();

                // do not repeatedly retrieve the same document
                if (redirects.existsRedirectTo(redirectUrl)) {
                    throw new RedirectToExistingDocumentException();
                }

                method.abort();

                try {
                    getUrl = new URL(redirectUrl);
                } catch (MalformedURLException e) {
                    throw new IOException(e);
                }
            } else {
                throw new ErrorResponseException("" + code + " response for resource <"
                        + StringUtils.escapeURIString(iri) + ">");
            }
        }

        // if we followed one or more redirects, record the redirection to save on future work
        if (null != redirectUrl) {
            try {
                redirects.persistRedirect(iri, redirectUrl);
            } catch (SailException e) {
                throw new IOException(e);
            }
        }

        // keep the connection open only if we did not exit abnormally
        method = null;

        return response;
    }

    /**
     * A helper InputStream which closes the HTTP connection when it itself is
     * closed
     */
    private class HttpRepresentationInputStream extends InputStream {
        private final InputStream innerInputStream;

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

    public class HTTPException extends IOException {
        public HTTPException() {
            super();
        }

        public HTTPException(final String message) {
            super(message);
        }
    }

    public class ErrorResponseException extends HTTPException {
        public ErrorResponseException(final String message) {
            super(message);
        }
    }

    public class InvalidResponseException extends HTTPException {
        public InvalidResponseException(final String message) {
            super(message);
        }
    }

    public class RedirectToExistingDocumentException extends HTTPException {
        public RedirectToExistingDocumentException() {
            super();
        }
    }
}
