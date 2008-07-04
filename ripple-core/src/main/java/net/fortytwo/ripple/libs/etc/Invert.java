/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.etc;

import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 4:25:16 PM
 */
public class Invert extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            // FIXME: this is a kludge for programs created by Ripple 0.5-dev.  Remove this alias when it is no longer needed
            EtcLibrary.NS_2007_08 + "invert",

            EtcLibrary.NS_2008_06 + "invert"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public int arity()
    {
        return 1;
    }

    public void applyTo( final StackContext arg,
                         final Sink<StackContext, RippleException> solutions ) throws RippleException
    {
        RippleList stack = arg.getStack();
        final ModelConnection mc = arg.getModelConnection();

        final RippleList rest = stack.getRest();

        RippleValue f = stack.getFirst();
//System.out.println("value to invert: " + f);
        
        Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
        {
            public void put( final Operator op ) throws RippleException
            {
//System.out.println("mapping to invert: " + op.getMapping());
                // Note: this operation both inverts and applies the mapping
                RippleValue inverse = new StackMappingWrapper( op.getMapping().inverse(), mc );
                solutions.put( arg.with( rest.push( inverse ) ) );
            }
        };

        Operator.createOperator(f, opSink, mc);
    }
}
