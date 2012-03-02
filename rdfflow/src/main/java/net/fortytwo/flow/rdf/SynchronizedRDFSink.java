/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/SynchronizedRDFSink.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.SynchronizedSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class SynchronizedRDFSink implements RDFSink {
	private final SynchronizedSink<Statement> stSink;
	private final SynchronizedSink<Namespace> nsSink;
	private final SynchronizedSink<String> comSink;

	public SynchronizedRDFSink( final RDFSink sink, final Object mutex )
	{
		stSink = new SynchronizedSink<Statement>( sink.statementSink(), mutex );
		nsSink = new SynchronizedSink<Namespace>( sink.namespaceSink(), mutex );
		comSink = new SynchronizedSink<String>( sink.commentSink(), mutex );
	}

	public Sink<Statement> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String> commentSink()
	{
		return comSink;
	}

    public Object getMutex()
    {
        return stSink.getMutex();
    }
}

