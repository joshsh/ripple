package net.fortytwo.linkeddata.rdfizers;

import net.fortytwo.linkeddata.CacheEntry;
import net.fortytwo.linkeddata.Rdfizer;
import org.apache.commons.io.IOUtils;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class VerbatimRdfizer implements Rdfizer {
    private static final Logger logger = LoggerFactory.getLogger(VerbatimRdfizer.class);

    private final RDFFormat format;
    private final RDFParser parser;

    public VerbatimRdfizer(final RDFFormat format,
                           final RDFParser.DatatypeHandling datatypeHandling) {
        this.format = format;
        parser = Rio.createParser(format);
        parser.setDatatypeHandling(datatypeHandling);
    }

    public CacheEntry.Status rdfize(final InputStream is,
                                    final RDFHandler handler,
                                    final String baseUri) {
        try {
            parser.setRDFHandler(handler);
            parser.parse(is, baseUri);
        } catch (IOException e) {
            logger.warn("I/O error in " + format.getName() + " rdfizer", e);
            return CacheEntry.Status.Failure;
        } catch (RDFParseException e) {
            logger.warn("RDF parsing error in " + format.getName() + " rdfizer", e);
            return CacheEntry.Status.ParseError;
        } catch (RDFHandlerException e) {
            logger.warn("RDF handler error in " + format.getName() + " rdfizer", e);
            return CacheEntry.Status.Failure;
        } catch (Throwable t) {
            logger.error("unclassified error in " + format.getName() + " rdfizer", t);
        }

        return CacheEntry.Status.Success;
    }

    public String toString() {
        return "'" + this.format.getName() + "' verbatim rdfizer";
    }
}
