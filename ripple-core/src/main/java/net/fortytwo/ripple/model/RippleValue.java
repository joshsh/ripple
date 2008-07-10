/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;

public interface RippleValue
{
	RdfValue toRDF( ModelConnection mc )
		throws RippleException;

	boolean isActive();

	void printTo( RipplePrintStream p )
		throws RippleException;
}

