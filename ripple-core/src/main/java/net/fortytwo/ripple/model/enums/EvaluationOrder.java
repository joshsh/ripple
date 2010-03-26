/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
* Date: Feb 29, 2008
* Time: 6:13:59 PM
*/
public enum EvaluationOrder
{
    EAGER  ( "eager" ),
    LAZY   ( "lazy" );

    private String name;

    private EvaluationOrder( final String name )
    {
        this.name = name;
    }

    public static EvaluationOrder find( final String name )
        throws RippleException
    {
        for ( EvaluationOrder order : EvaluationOrder.values() )
        {
            if ( order.name.equals( name ) )
            {
                return order;
            }
        }

        throw new RippleException( "unknown EvaluationOrder: " + name );
    }
}
