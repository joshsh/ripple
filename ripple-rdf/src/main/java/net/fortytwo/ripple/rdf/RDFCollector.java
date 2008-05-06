/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Source;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFCollector extends RDFSource implements RDFSink
{
	private Collector<Statement, RippleException> statements;
	private Collector<Namespace, RippleException> namespaces;
	private Collector<String, RippleException> comments;

	public RDFCollector()
	{
		statements = new Collector<Statement, RippleException>();
		namespaces = new Collector<Namespace, RippleException>();
		comments = new Collector<String, RippleException>();
	}

	public Sink<Statement, RippleException> statementSink()
	{
		return statements;
	}

	public Sink<Namespace, RippleException> namespaceSink()
	{
		return namespaces;
	}

	public Sink<String, RippleException> commentSink()
	{
		return comments;
	}

	public Source<Statement, RippleException> statementSource()
	{
		return statements;
	}

	public Source<Namespace, RippleException> namespaceSource()
	{
		return namespaces;
	}

	public Source<String, RippleException> commentSource()
	{
		return comments;
	}

	public void writeTo( final RDFSink sink ) throws RippleException
	{
		statements.writeTo( sink.statementSink() );
		namespaces.writeTo( sink.namespaceSink() );
		comments.writeTo( sink.commentSink() );
	}

	public void clear()
	{
		statements.clear();
		namespaces.clear();
		comments.clear();
	}

	public int countStatements()
	{
		return statements.size();
	}

	public int countNamespaces()
	{
		return namespaces.size();
	}

	public int countComments()
	{
		return comments.size();
	}
}

