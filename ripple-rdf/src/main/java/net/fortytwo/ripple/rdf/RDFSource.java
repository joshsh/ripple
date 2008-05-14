/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Source;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public abstract class RDFSource
{
	public abstract Source<Statement, RippleException> statementSource() throws RippleException;
	public abstract Source<Namespace, RippleException> namespaceSource() throws RippleException;
	public abstract Source<String, RippleException> commentSource() throws RippleException;

	public void writeTo( final RDFSink sink ) throws RippleException
	{
		commentSource().writeTo( sink.commentSink() );

		// Note: it's often important that namespaces are written before
		// statements.
		namespaceSource().writeTo( sink.namespaceSink() );

		statementSource().writeTo( sink.statementSink() );
	}
}

