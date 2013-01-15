package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Closure implements StackMapping
{
	private final StackMapping innerMapping;
	private final RippleValue argument;
	private final int calculatedArity;

    /**
     *
     * @param innerMapping a mapping
     * @param argument an inactive value
     */
    public Closure( final StackMapping innerMapping, final RippleValue argument )
	{
		this.innerMapping = innerMapping;
		this.argument = argument;
		calculatedArity = innerMapping.arity() - 1;
	}

	public int arity()
	{
		return calculatedArity;
	}

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        innerMapping.apply( arg.push( argument ), solutions, mc );
	}
	
	public boolean isTransparent()
	{
		return innerMapping.isTransparent();
	}
	
	public String toString()
	{
		return "Closure(" + innerMapping + ", " + argument + ")";
	}

    public StackMapping getInverse() throws RippleException
    {
        return new Closure( innerMapping.getInverse(), argument );
    }
}

