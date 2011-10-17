/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFNullSink.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFNullSink implements RDFSink {
	private final Sink<Statement> stSink = new NullSink<Statement>();
	private final Sink<Namespace> nsSink = new NullSink<Namespace>();
	private final Sink<String> cmtSink = new NullSink<String>();

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
		return cmtSink;
	}
}

