/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata;

import net.fortytwo.ripple.RippleException;
import org.restlet.resource.Representation;

public interface Dereferencer
{
	/**
	 *
	 * @param uri
	 * @return
	 * @throws RippleException
	 */
	// TODO: this method throws an exception, while Rdfizer.handle does not
	Representation dereference( String uri ) throws RippleException;
}

