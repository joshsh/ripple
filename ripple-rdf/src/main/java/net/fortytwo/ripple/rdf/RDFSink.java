/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public interface RDFSink<E extends Exception>
{
	Sink<Statement, E> statementSink();
	Sink<Namespace, E> namespaceSink();
	Sink<String, E> commentSink();
}

