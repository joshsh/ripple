package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.flow.Sink;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface AST<T>
{
	void evaluate( Sink<T> sink,
					QueryEngine qe,
					ModelConnection mc )
		throws RippleException;
}

