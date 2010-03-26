/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Tee;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFTee<E extends Exception> implements RDFSink<E>
{
	private final Sink<Statement, E> statementTee;
	private final Sink<Namespace, E> namespaceTee;
	private final Sink<String, E> commentTee;

	public RDFTee( final RDFSink<E> sinkA, final RDFSink<E> sinkB )
	{
		statementTee = new Tee<Statement, E>( sinkA.statementSink(), sinkB.statementSink() );
		namespaceTee = new Tee<Namespace, E>( sinkA.namespaceSink(), sinkB.namespaceSink() );
		commentTee = new Tee<String, E>( sinkA.commentSink(), sinkB.commentSink() );
	}

	public Sink<Statement, E> statementSink()
	{
		return statementTee;
	}

	public Sink<Namespace, E> namespaceSink()
	{
		return namespaceTee;
	}

	public Sink<String, E> commentSink()
	{
		return commentTee;
	}
}

