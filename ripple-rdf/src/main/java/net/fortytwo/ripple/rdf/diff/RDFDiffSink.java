/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.flow.diff.DiffSink;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public interface RDFDiffSink<E extends Exception>
{
	RDFSink<E> adderSink();
	RDFSink<E> subtractorSink();

    DiffSink<Statement, E> statementSink();
    DiffSink<Namespace, E> namespaceSink();
    DiffSink<String, E> commentSink();
}

