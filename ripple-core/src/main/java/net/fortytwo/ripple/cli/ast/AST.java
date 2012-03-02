/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.flow.Sink;

public interface AST<T>
{
	void evaluate( Sink<T> sink,
					QueryEngine qe,
					ModelConnection mc )
		throws RippleException;
}

