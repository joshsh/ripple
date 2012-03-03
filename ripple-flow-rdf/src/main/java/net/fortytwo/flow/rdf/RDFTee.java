/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFTee.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Tee;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFTee implements RDFSink {
	private final Sink<Statement> statementTee;
	private final Sink<Namespace> namespaceTee;
	private final Sink<String> commentTee;

	public RDFTee( final RDFSink sinkA, final RDFSink sinkB )
	{
		statementTee = new Tee<Statement>( sinkA.statementSink(), sinkB.statementSink() );
		namespaceTee = new Tee<Namespace>( sinkA.namespaceSink(), sinkB.namespaceSink() );
		commentTee = new Tee<String>( sinkA.commentSink(), sinkB.commentSink() );
	}

	public Sink<Statement> statementSink()
	{
		return statementTee;
	}

	public Sink<Namespace> namespaceSink()
	{
		return namespaceTee;
	}

	public Sink<String> commentSink()
	{
		return commentTee;
	}
}

