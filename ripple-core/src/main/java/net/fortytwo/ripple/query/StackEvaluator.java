/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * Author: josh
 * Date: Feb 11, 2008
 * Time: 3:15:35 PM
 */
public abstract class StackEvaluator extends Evaluator<RippleList, RippleList, ModelConnection> implements StackMapping
{
	public int arity()
	{
		return 1;
	}

    // TODO
    public StackMapping getInverse() throws RippleException
    {
        return new NullStackMapping();
    }
}
