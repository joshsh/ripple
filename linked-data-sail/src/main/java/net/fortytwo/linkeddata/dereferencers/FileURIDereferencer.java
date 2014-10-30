package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.linkeddata.Dereferencer;
import net.fortytwo.ripple.RippleException;
import org.openrdf.rio.RDFFormat;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class FileURIDereferencer implements Dereferencer {
    public Representation dereference(final String uri) throws RippleException {
        return new FileRepresentation(uri);
    }

    public static MediaType findMediaType(final String uri) throws RippleException {
        RDFFormat format = RDFFormat.forFileName(uri);
        if (null == format) {
            throw new RippleException("no matching media type for " + uri);
        }

        List<String> types = format.getMIMETypes();
        if (0 == types.size()) {
            throw new IllegalStateException("RDF format has no media type(s): " + format);
        }

        return new MediaType(types.iterator().next());
    }

    private class FileRepresentation extends StreamRepresentation {
        private InputStream inputStream;

        public FileRepresentation(final String uri) throws RippleException {
            super(findMediaType(uri));

            try {
                inputStream = new FileInputStream(uri.substring(5));
            } catch (IOException e) {
                throw new RippleException(e);
            }
        }

        public ReadableByteChannel getChannel() throws IOException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public InputStream getStream() throws IOException {
            return inputStream;
        }

        public void write(OutputStream outputStream) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void write(WritableByteChannel writableByteChannel) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public String toString() {
        return "file URI dereferencer";
    }
}
