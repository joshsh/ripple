package net.fortytwo.linkeddata.rdfizers;

import net.fortytwo.linkeddata.CacheEntry;
import net.fortytwo.linkeddata.Rdfizer;
import net.fortytwo.ripple.RippleException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class VerbatimRdfizer implements Rdfizer {
    private final RDFFormat format;
    private final RDFParser parser;

    public VerbatimRdfizer(final RDFFormat format) {
        this.format = format;
        parser = Rio.createParser(format);
        parser.setDatatypeHandling(RDFParser.DatatypeHandling.IGNORE);
    }

    public CacheEntry.Status rdfize(final InputStream is,
                                     final RDFHandler handler,
                                     final String baseUri) {
        try {
            parser.setRDFHandler(handler);
            parser.parse(is, baseUri);
        } catch (IOException e) {
            new RippleException(e).logError(false);
            return CacheEntry.Status.Failure;
        } catch (RDFParseException e) {
            new RippleException(e).logError(false);
            return CacheEntry.Status.ParseError;
        } catch (RDFHandlerException e) {
            new RippleException(e).logError(false);
            return CacheEntry.Status.Failure;
        }

        return CacheEntry.Status.Success;
    }

    public String toString() {
        return "'" + this.format.getName() + "' verbatim rdfizer";
    }
}
