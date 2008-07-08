/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 5:14:03 PM
 */
public class StackMappingWrapper extends PrimitiveStackMapping
{
    // FIXME: awkward
    private static final String[] IDENTIFIERS = {};
    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    private final StackMapping innerMapping;

    public StackMappingWrapper( final StackMapping wrapped, final ModelConnection mc ) throws RippleException
    {
        super( wrapped.isTransparent() );
        this.innerMapping = wrapped;
        
        // Uses a random identifier... not for actual use
        setRdfEquivalent( new RdfValue( mc.createBNode() ), mc );
//System.out.println("created a StackMappingWrapper: " + this + " for mapping " + innerMapping);
    }

    public int arity()
    {
        return innerMapping.arity();
    }

    public void applyTo( final StackContext arg, final Sink<StackContext, RippleException> solutions ) throws RippleException
    {
        innerMapping.applyTo( arg, solutions );
    }

    public StackMapping inverse() throws RippleException
    {
        return innerMapping.inverse();
    }
}
