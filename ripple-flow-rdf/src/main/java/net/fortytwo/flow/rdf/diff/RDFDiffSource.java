package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSource;
import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RDFDiffSource {
    RDFSource adderSource();

    RDFSource subtractorSource();

    void writeTo(RDFDiffSink sink) throws RippleException;
}

