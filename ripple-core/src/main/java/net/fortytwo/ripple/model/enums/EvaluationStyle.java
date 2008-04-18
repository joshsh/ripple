/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
* Date: Feb 29, 2008
* Time: 6:14:12 PM
*/
public enum EvaluationStyle
{
    APPLICATIVE    ( "applicative" ),
    COMPOSITIONAL  ( "compositional" );

    private String name;

    private EvaluationStyle( final String name )
    {
        this.name = name;
    }

    public static EvaluationStyle find( final String name )
        throws RippleException
    {
        for ( EvaluationStyle style : EvaluationStyle.values() )
        {
            if ( style.name.equals( name ) )
            {
                return style;
            }
        }

        throw new RippleException( "unknown EvaluationStyle: " + name );
    }
}
