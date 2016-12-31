package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.linkeddata.Dereferencer;
import net.fortytwo.linkeddata.LinkedDataCache;
import org.openrdf.rio.RDFFormat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class FileURIDereferencer implements Dereferencer {
    public LinkedDataCache.Representation dereference(final String uri) {
        return new FileRepresentation(uri);
    }

    public static String findMediaType(final String uri) {
        Optional<RDFFormat> format = RDFFormat.matchFileName(uri, null);
        if (!format.isPresent()) {
            throw new IllegalArgumentException("no matching media type for " + uri);
        }

        List<String> types = format.get().getMIMETypes();
        if (0 == types.size()) {
            throw new IllegalStateException("RDF format has no media type(s): " + format);
        }

        return types.iterator().next();
    }

    private class FileRepresentation extends LinkedDataCache.Representation {
        private InputStream inputStream;

        public FileRepresentation(final String uri) {
            super(findMediaType(uri));

            try {
                inputStream = new FileInputStream(uri.substring(5));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public InputStream getStream() {
            return inputStream;
        }
    }

    public String toString() {
        return "file URI dereferencer";
    }
}
