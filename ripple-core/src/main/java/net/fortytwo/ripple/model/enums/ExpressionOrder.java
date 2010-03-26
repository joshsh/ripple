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
* Time: 6:14:27 PM
*/
public enum ExpressionOrder
{
    DIAGRAMMATIC      ( "diagrammatic" ),
    ANTIDIAGRAMMATIC  ( "antidiagrammatic" );

    private String name;

    private ExpressionOrder( final String n )
    {
        name = n;
    }

    public static ExpressionOrder find( final String name )
        throws RippleException
    {
        for ( ExpressionOrder x : ExpressionOrder.values() )
        {
            if ( x.name.equals( name ) )
            {
                return x;
            }
        }

        throw new RippleException( "unknown ExpressionOrder: '" + name + "'" );
    }
}
