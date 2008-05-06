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
import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFNullSink implements RDFSink
{
	private Sink<Statement, RippleException> stSink = new NullSink<Statement, RippleException>();
	private Sink<Namespace, RippleException> nsSink = new NullSink<Namespace, RippleException>();
	private Sink<String, RippleException> cmtSink = new NullSink<String, RippleException>();

	public Sink<Statement, RippleException> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, RippleException> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, RippleException> commentSink()
	{
		return cmtSink;
	}
}

