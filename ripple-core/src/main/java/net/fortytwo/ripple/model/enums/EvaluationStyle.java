package net.fortytwo.ripple.model.enums;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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
