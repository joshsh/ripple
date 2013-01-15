package net.fortytwo.ripple.query;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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
