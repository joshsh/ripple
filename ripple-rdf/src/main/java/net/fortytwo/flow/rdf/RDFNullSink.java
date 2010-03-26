/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFNullSink<E extends Exception> implements RDFSink<E>
{
	private final Sink<Statement, E> stSink = new NullSink<Statement, E>();
	private final Sink<Namespace, E> nsSink = new NullSink<Namespace, E>();
	private final Sink<String, E> cmtSink = new NullSink<String, E>();

	public Sink<Statement, E> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, E> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, E> commentSink()
	{
		return cmtSink;
	}
}

