/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFSink.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public interface RDFSink {
	Sink<Statement> statementSink();
	Sink<Namespace> namespaceSink();
	Sink<String> commentSink();
}

