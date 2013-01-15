package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.diff.DiffSink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RDFDiffSink {
	RDFSink adderSink();
	RDFSink subtractorSink();

    DiffSink<Statement> statementSink();
    DiffSink<Namespace> namespaceSink();
    DiffSink<String> commentSink();
}

