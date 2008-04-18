/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public interface RDFSink
{
	Sink<Statement, RippleException> statementSink();
	Sink<Namespace, RippleException> namespaceSink();
	Sink<String, RippleException> commentSink();
}

// kate: tab-width 4
