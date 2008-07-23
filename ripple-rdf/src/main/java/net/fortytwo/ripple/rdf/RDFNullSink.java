/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.flow.Sink;

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

