package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.restlet.data.MediaType;
import org.restlet.representation.StreamRepresentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPRepresentation extends StreamRepresentation {
    private final InputStream inputStream;
    private final HttpMethod method;

    private final long idleTime;

    // Note: the URI is immediately dereferenced
    public HTTPRepresentation(final String uri, final String acceptHeader) throws RippleException {
        super(null);

        method = HTTPUtils.createGetMethod(uri);
        HTTPUtils.setAcceptHeader(method, acceptHeader);
        idleTime = HTTPUtils.throttleHttpRequest(method);

        HttpClient client = HTTPUtils.createClient();

        try {
            client.executeMethod(method);
        } catch (ConnectTimeoutException e) {

        } catch (IOException e) {
            throw new RippleException(e);
        }

        InputStream is;

        int code = method.getStatusCode();

        if (2 != code / 100) {
            throw new ErrorResponseException("" + code + " response for resource <"
                    + StringUtils.escapeURIString(uri) + ">");
        }

        try {
            is = method.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RippleException(e);
        }

        inputStream = new HttpRepresentationInputStream(is);

        Header h = method.getResponseHeader(HTTPUtils.CONTENT_TYPE);
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

    /**
     * @return the HTTP method of this representation.
     * This class is generally used as an internal component of LinkedDataSail,
     * but it can also be used as a standalone tool for dereferencing Linked Data
     * URIs, in which case access to HTTP headers and status data is useful.
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @return the amount of time, in milliseconds, which was spent on a courtesy delay
     * (to avoid overloading remote servers) while creating this representation, as opposed
     * to time spent waiting for a response from the remote server or receiving packet data.
     * This is important for accurate response time analysis.
     */
    public long getIdleTime() {
        return idleTime;
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
            method.releaseConnection();

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

        public HttpMethod getMethod() {
            return method;
        }
    }

    public class InvalidResponseException extends RippleException {
        public InvalidResponseException(final String message) {
            super(message);
        }

        public HttpMethod getMethod() {
            return method;
        }
    }
}
