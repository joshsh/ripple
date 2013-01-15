package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RDFSink {
	Sink<Statement> statementSink();
	Sink<Namespace> namespaceSink();
	Sink<String> commentSink();
}

