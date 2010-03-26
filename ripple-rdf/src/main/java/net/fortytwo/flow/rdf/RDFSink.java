/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public interface RDFSink<E extends Exception>
{
	Sink<Statement, E> statementSink();
	Sink<Namespace, E> namespaceSink();
	Sink<String, E> commentSink();
}

