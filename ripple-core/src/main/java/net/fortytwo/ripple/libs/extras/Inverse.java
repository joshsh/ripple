/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMappingWrapper;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 4:25:16 PM
 */
public class Inverse extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            ExtrasLibrary.NS_2008_08 + "invert",
            ExtrasLibrary.NS_2011_04 + "inverse"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "mapping", null, true )};
    }

    public String getComment()
    {
        return "consumes a mapping and produces the inverse of that mapping (or a null mapping if the inverse is not otherwise defined)";
    }

    public void apply( final StackContext arg,
                         final Sink<StackContext, RippleException> solutions ) throws RippleException
    {
        RippleList stack = arg.getStack();
        final ModelConnection mc = arg.getModelConnection();

        final RippleList rest = stack.getRest();
        RippleValue f = stack.getFirst();

        Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
        {
            public void put( final Operator op ) throws RippleException
            {
                RippleValue inverse = new StackMappingWrapper( op.getMapping().getInverse(), mc );
                solutions.put( arg.with( rest.push( inverse ) ) );
            }
        };

        Operator.createOperator( f, opSink, mc );
    }
}
