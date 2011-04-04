/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFTee.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

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

