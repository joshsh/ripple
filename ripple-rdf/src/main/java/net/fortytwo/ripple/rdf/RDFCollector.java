/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Source;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFCollector<E extends Exception> extends RDFSource<E> implements RDFSink<E>
{
	private final Collector<Statement, E> statements;
	private final Collector<Namespace, E> namespaces;
	private final Collector<String, E> comments;

	public RDFCollector()
	{
		statements = new Collector<Statement, E>();
		namespaces = new Collector<Namespace, E>();
		comments = new Collector<String, E>();
	}

	public Sink<Statement, E> statementSink()
	{
		return statements;
	}

	public Sink<Namespace, E> namespaceSink()
	{
		return namespaces;
	}

	public Sink<String, E> commentSink()
	{
		return comments;
	}

	public Source<Statement, E> statementSource()
	{
		return statements;
	}

	public Source<Namespace, E> namespaceSource()
	{
		return namespaces;
	}

	public Source<String, E> commentSource()
	{
		return comments;
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

