/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.URI;

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
        URI uri = mc.createURI( qe.getDefaultNamespace() + term );
        mc.remove( new RDFValue(uri), null, null );
		mc.commit();
        mc.getModel().getSpecialValues().remove( uri );        
    }

	protected void abort()
	{
	}
}

