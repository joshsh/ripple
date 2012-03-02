/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffSink.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.diff.DiffSink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public interface RDFDiffSink {
	RDFSink adderSink();
	RDFSink subtractorSink();

    DiffSink<Statement> statementSink();
    DiffSink<Namespace> namespaceSink();
    DiffSink<String> commentSink();
}

