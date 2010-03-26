/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;

public class RedefineTermCmd extends Command
{
	private final UndefineTermCmd undefineCmd;
    private final DefineTermCmd defineCmd;

	public RedefineTermCmd( final String term, final ListAST ast )
	{
		undefineCmd = new UndefineTermCmd( term );
		defineCmd = new DefineTermCmd( term, ast );
	}

    public String getName()
    {
        return defineCmd.getName();
    }

    public ListAST getList()
    {
        return defineCmd.getList();
    }

    public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		undefineCmd.execute( qe, mc );
		defineCmd.execute( qe, mc );
	}

	protected void abort()
	{
	}
}

