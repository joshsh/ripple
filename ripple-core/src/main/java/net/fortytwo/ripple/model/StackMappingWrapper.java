/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
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
        rdfValue = new ModelConnectionHelper(mc).createRandomURI().toRDF(mc);
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

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        innerMapping.apply( arg, solutions, mc );
    }

    public StackMapping getInverse() throws RippleException
    {
        return innerMapping.getInverse();
    }

    public RDFValue toRDF( final ModelConnection mc ) throws RippleException
    {
        return rdfValue;
    }

    public StackMapping getMapping() {
        return null;
    }

    public void printTo( final RipplePrintStream p ) throws RippleException
    {
        p.print( "[StackMappingWrapper: " );
        p.print( innerMapping );
        p.print( "]" );
    }

    public Type getType() {
        return Type.OTHER_RESOURCE;
    }
}
