/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;

public interface RippleValue
{
	RDFValue toRDF( ModelConnection mc )
		throws RippleException;

	boolean isActive();

	void printTo( RipplePrintStream p )
		throws RippleException;
}

