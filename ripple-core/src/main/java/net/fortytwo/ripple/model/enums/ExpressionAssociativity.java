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
* Time: 6:12:50 PM
*/
public enum ExpressionAssociativity
{
    LEFT   ( "left" ),
    RIGHT  ( "right" );

    private String name;

    private ExpressionAssociativity( final String n )
    {
        name = n;
    }

    public static ExpressionAssociativity find( final String name )
        throws RippleException
    {
        for ( ExpressionAssociativity x : ExpressionAssociativity.values() )
        {
            if ( x.name.equals( name ) )
            {
                return x;
            }
        }

        String msg = "unknown ExpressionAssociativity: '" + name + "'";
        throw new RippleException( msg );
    }
}
