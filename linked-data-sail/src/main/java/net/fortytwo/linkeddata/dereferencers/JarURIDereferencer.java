package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.linkeddata.Dereferencer;
import net.fortytwo.linkeddata.LinkedDataCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class JarURIDereferencer implements Dereferencer {
    public LinkedDataCache.Representation dereference(final String uri) {
        return new JarRepresentation(uri);
    }

    private class JarRepresentation extends LinkedDataCache.Representation {
        private final InputStream inputStream;

        public JarRepresentation(final String uri) {
            super(FileURIDereferencer.findMediaType(uri));

            JarURLConnection jc;

            try {
                jc = (JarURLConnection) (new URL(uri).openConnection());
                inputStream = jc.getInputStream();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public InputStream getStream() {
            return inputStream;
        }
    }

    public String toString() {
        return "JAR URI dereferencer";
    }
}
