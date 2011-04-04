/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

public class UndefineTermCmd extends Command
{
	private final String term;

	public UndefineTermCmd( final String term )
	{
		this.term = term;
	}

	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
        RDFValue uri = mc.uriValue( qe.getDefaultNamespace() + term );
//System.out.println("uri = " + uri);
        mc.remove( uri, null, null );
		mc.commit();
        mc.getModel().getSpecialValues().remove( uri.sesameValue() );
    }

	protected void abort()
	{
	}
}

