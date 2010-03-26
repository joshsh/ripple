/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

import org.openrdf.model.URI;

public class DefineTermCmd extends Command
{
	private final ListAST list;
	private final String name;

	public DefineTermCmd( final String name, final ListAST list)
	{
		this.list = list;
		this.name = name;
	}
        
    public String getName()
    {
        return name;
    }

    public ListAST getList()
    {
        return list;
    }

    public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		Collector<RippleList, RippleException> expressions = new Collector<RippleList, RippleException>();
		list.evaluate( expressions, qe, mc );

		if ( expressions.size() == 0 )
		{
			qe.getErrorPrintStream().println(
				"Warning: the given expression did not resolve to a value." );
		}

		else if ( expressions.size() > 1 )
		{
			qe.getErrorPrintStream().println(
				"Warning: the given expression resolved to multiple values." );
		}

		else
		{
			// Note: the first element of the list will also be a list
			RippleList expr = (RippleList) expressions.iterator().next().getFirst();

//System.out.println( "exprList = " + exprList );

// TODO: check for collision with an existing URI
			URI uri = mc.createURI( qe.getDefaultNamespace() + name);
			mc.copyStatements( expr, new RDFValue( uri ) );
			mc.commit();

			qe.getLexicon().addURI( uri );
            mc.getModel().getSpecialValues().put( uri, expr );
        }
	}

	protected void abort()
	{
	}
}

