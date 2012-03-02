/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

public class UndefineListCmd extends Command
{
	private final String term;

	public UndefineListCmd(final String term)
	{
		this.term = term;
	}

	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
        RDFValue uri = mc.uriValue(qe.getLexicon().getDefaultNamespace() + term);
//System.out.println("uri = " + uri);
        mc.remove( uri, null, null );
		mc.commit();
        mc.getModel().getSpecialValues().remove( uri.sesameValue() );
    }

    public String getName() {
        return "unlist";
    }

    protected void abort()
	{
	}
}

