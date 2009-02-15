/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import java.util.Properties;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

public class AnnotatedAST implements AST<RippleList>
{
	private final AST innerAst;
	private final Properties props;
	
	public AnnotatedAST( final AST inner, final Properties props )
	{
		innerAst = inner;
		this.props = props;
	}
	
	public void evaluate( Sink<RippleList, RippleException> sink,
					QueryEngine qe,
					ModelConnection mc )
		throws RippleException
	{
// TODO: create a PropertyAnnotatedValue class and translate into it
		innerAst.evaluate( sink, qe, mc );
	}
}


