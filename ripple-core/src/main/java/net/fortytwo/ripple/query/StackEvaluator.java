/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Feb 11, 2008
 * Time: 3:15:35 PM
 */
public abstract class StackEvaluator extends Evaluator<StackContext, StackContext, RippleException> implements StackMapping
{
	public int arity()
	{
		return 1;
	}

    // TODO
    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }
}
