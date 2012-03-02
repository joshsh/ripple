package net.fortytwo.linkeddata.util;

import org.openrdf.rio.RDFFormat;
import org.restlet.data.MediaType;

import java.util.List;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFUtil {

    public static MediaType findMediaType(final RDFFormat format) {
        List<String> types = format.getMIMETypes();
        if (null == types || 0 == types.size()) {
            throw new IllegalStateException("no MIME type for RDF format: " + format);
        }

        return new MediaType(types.iterator().next());
    }
}
