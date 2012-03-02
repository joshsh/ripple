/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFCollector.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class RDFCollector extends RDFSource implements RDFSink {
	private final Collector<Statement> statements;
	private final Collector<Namespace> namespaces;
	private final Collector<String> comments;

	public RDFCollector()
	{
		statements = new Collector<Statement>();
		namespaces = new Collector<Namespace>();
		comments = new Collector<String>();
	}

	public Sink<Statement> statementSink()
	{
		return statements;
	}

	public Sink<Namespace> namespaceSink()
	{
		return namespaces;
	}

	public Sink<String> commentSink()
	{
		return comments;
	}

	public Source<Statement> statementSource()
	{
		return statements;
	}

	public Source<Namespace> namespaceSource()
	{
		return namespaces;
	}

	public Source<String> commentSource()
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

