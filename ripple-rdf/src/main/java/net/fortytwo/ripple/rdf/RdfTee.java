/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Tee;
import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RdfTee implements RDFSink
{
	private final Sink<Statement, RippleException> statementTee;
	private final Sink<Namespace, RippleException> namespaceTee;
	private final Sink<String, RippleException> commentTee;

	public RdfTee( final RDFSink sinkA, final RDFSink sinkB )
	{
		statementTee = new Tee<Statement, RippleException>( sinkA.statementSink(), sinkB.statementSink() );
		namespaceTee = new Tee<Namespace, RippleException>( sinkA.namespaceSink(), sinkB.namespaceSink() );
		commentTee = new Tee<String, RippleException>( sinkA.commentSink(), sinkB.commentSink() );
	}

	public Sink<Statement, RippleException> statementSink()
	{
		return statementTee;
	}

	public Sink<Namespace, RippleException> namespaceSink()
	{
		return namespaceTee;
	}

	public Sink<String, RippleException> commentSink()
	{
		return commentTee;
	}
}

