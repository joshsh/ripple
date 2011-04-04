/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/stream/Both.java $
 * $Revision: 106 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;

/**
 *
 */
// FIXME: total hack
public class Count extends PrimitiveStackMapping
{
    private int count;

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2008_08 + "count"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Count()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {};
    }

    public String getComment()
    {
        return "-> count for last call of lastCount (disclaimer: this is a total hack)";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();

        RippleList stack = arg.getStack();

        // Push the last result, in a stack of its own.
        solutions.put( arg.with( mc.list( mc.value( count ) ) ) );

        // Push the code to "count" the current argument stack.
        solutions.put( arg.with( stack.push( new Operator( new Counter() ) ) ) );
	}

    private class Counter implements StackMapping
    {
        public Counter()
        {
            count = 0;
        }

        public int arity()
        {
            return 1;
        }

        public StackMapping inverse() throws RippleException
        {
            return new NullStackMapping();
        }

        public boolean isTransparent()
        {
            return true;
        }

        public void apply( final StackContext arg,
                           final Sink<StackContext, RippleException> solutions ) throws RippleException
        {
            count++;
        }
    }
}
