/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.util.ModelConnectionHelper;

/**
 * FIXME: this should probably not be a RippleValue.  Perhaps the behavior of 'invert' should change so that
 * this is not necessary.
 *
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 5:14:03 PM
 */
public class StackMappingWrapper implements StackMapping, RippleValue
{
    private final RDFValue rdfValue;

    private final StackMapping innerMapping;

    public StackMappingWrapper( final StackMapping wrapped, final ModelConnection mc ) throws RippleException
    {
        this.innerMapping = wrapped;
        
        // Uses a random identifier... not for actual use
        rdfValue = new RDFValue( new ModelConnectionHelper(mc).createRandomURI() );
//System.out.println("created a StackMappingWrapper: " + this + " for mapping " + innerMapping);
    }

    public int arity()
    {
        return innerMapping.arity();
    }

    public boolean isTransparent()
    {
        return innerMapping.isTransparent();
    }

    public void apply( final StackContext arg, final Sink<StackContext, RippleException> solutions ) throws RippleException
    {
        innerMapping.apply( arg, solutions );
    }

    public StackMapping inverse() throws RippleException
    {
        return innerMapping.inverse();
    }

    public RDFValue toRDF( final ModelConnection mc ) throws RippleException
    {
        return rdfValue;
    }

    public boolean isActive() {
        return false;
    }

    public void printTo( final RipplePrintStream p ) throws RippleException
    {
        p.print( "wrapper[" );
        p.print( innerMapping );
        p.print( "]" );
    }
}
