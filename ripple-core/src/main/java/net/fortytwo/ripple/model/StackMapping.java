/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Mapping;

public interface StackMapping extends Mapping<StackContext, StackContext, RippleException>
{
	/**
	*  The fixed number of arguments which this function consumes before
	*  yielding a result.
	*/
	int arity();

    StackMapping inverse() throws RippleException;
}
