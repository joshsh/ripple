/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFUtils.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.linkeddata;

import net.fortytwo.flow.rdf.SesameOutputAdapter;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.OutputStream;
import java.util.UUID;


public final class RDFUtils {
    private RDFUtils() {
    }

    public static SesameOutputAdapter createOutputAdapter(
            final OutputStream out,
            final RDFFormat format)
            throws RippleException {
        RDFWriter writer;

        try {
            // Note: a comment by Jeen suggests that a new writer should be created
            //       for each use:
            //       http://www.openrdf.org/forum/mvnforum/viewthread?thread=785#3159
            writer = Rio.createWriter(format, out);
        } catch (Throwable t) {
            throw new RippleException(t);
        }

        return new SesameOutputAdapter(writer);
    }

    ////////////////////////////////////////////////////////////////////////////

    public static boolean isHttpUri(final URI uri) {
        return uri.toString().startsWith("http://");
    }

    public static URI inferContextURI(final Resource subject,
                                      final ValueFactory valueFactory)
            throws RippleException {
        if (!(subject instanceof URI)) {
            return null;
        } else {
            String s = removeFragmentIdentifier((subject).toString());

            try {
                return valueFactory.createURI(s);
            } catch (Throwable t) {
                throw new RippleException(t);
            }
        }
    }

    /**
     * Strips the fragment identifier of a (usually) HTTP URI.
     * @param uri a URI possibly containing a fragment identifier, e.g. http://example.org/foo#bar
     * @return the URI without a fragment identifier, e.g. http://example.org/foo
     */
    public static String removeFragmentIdentifier(final String uri) {
        int i = uri.lastIndexOf('#');
        return 0 <= i ? uri.substring(0, i) : uri;
    }

    // Note: using hashed URIs for graph names avoids collision with resource URIs in retrieved descriptions
   public static String findGraphUri(final String uri) {
        String docUri = removeFragmentIdentifier(uri);
        return Ripple.RANDOM_URN_PREFIX
                + UUID.nameUUIDFromBytes(docUri.getBytes());
    }
}
