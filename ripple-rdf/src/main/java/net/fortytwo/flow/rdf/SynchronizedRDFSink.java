/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.SynchronizedSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class SynchronizedRDFSink<E extends Exception> implements RDFSink<E>
{
	private final SynchronizedSink<Statement, E> stSink;
	private final SynchronizedSink<Namespace, E> nsSink;
	private final SynchronizedSink<String, E> comSink;

	public SynchronizedRDFSink( final RDFSink<E> sink, final Object mutex )
	{
		stSink = new SynchronizedSink<Statement, E>( sink.statementSink(), mutex );
		nsSink = new SynchronizedSink<Namespace, E>( sink.namespaceSink(), mutex );
		comSink = new SynchronizedSink<String, E>( sink.commentSink(), mutex );
	}

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
		return comSink;
	}

    public Object getMutex()
    {
        return stSink.getMutex();
    }
}

