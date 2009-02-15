/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.etc.EtcLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

public class OperatorAST implements AST<RippleList>
{
	public enum Type {
        Apply,
        Option,
        Star,
        Plus,
        Times,
        Range };

	private final Type type;
    private final boolean inverse;
    private NumberAST min, max;

	public OperatorAST( final Type type, boolean inverse )
	{
		this.type = type;
        this.inverse = inverse;
    }

    public OperatorAST()
    {
        this( Type.Apply, false );
    }

    public OperatorAST( final NumberAST times, boolean inverse )
	{
		type = Type.Times;
        this.inverse = inverse;
        min = times;
	}

	public OperatorAST( final NumberAST min, final NumberAST max, boolean inverse )
	{
		type = Type.Range;
        this.inverse = inverse;
        this.min = min;
		this.max = max;
	}

	public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		RippleList l;

		switch ( type )
		{
			case Apply:
				l = inverse
                        ? mc.list( Operator.OP )
                                .push( Operator.OP )
                                .push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP );
				break;
            case Option:
				l = inverse
                        ? mc.list( Operator.OP )
                                .push( StackLibrary.getOptApplyValue() )
                                .push( Operator.OP )
                                .push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP )
                                .push( StackLibrary.getOptApplyValue() );
				break;
            case Plus:
                l = inverse
                        ? mc.list( Operator.OP )
                                .push( StackLibrary.getPlusApplyValue() )
                                .push( Operator.OP )
                                .push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP )
                                .push( StackLibrary.getPlusApplyValue() );
                break;
            case Range:
                l = inverse
                        ? mc.list( Operator.OP )
                                .push( StackLibrary.getRangeApplyValue() )
                                .push( max.getValue( mc ) )
                                .push( min.getValue( mc ) ).push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP )
                                .push( StackLibrary.getRangeApplyValue() )
                                .push( max.getValue( mc ) )
                                .push( min.getValue( mc ) );
                break;
			case Star:
				l = inverse
                        ? mc.list( Operator.OP )
                                .push( StackLibrary.getStarApplyValue() )
                                .push( Operator.OP )
                                .push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP )
                                .push( StackLibrary.getStarApplyValue() );
				break;
            case Times:
                l = inverse
                        ? mc.list( Operator.OP )
                                .push( StackLibrary.getRangeApplyValue() )
                                .push( max.getValue( mc ) )
                                .push( min.getValue( mc ) ).push( EtcLibrary.getInvertValue() )
                        : mc.list( Operator.OP )
                                .push( StackLibrary.getTimesApplyValue() )
                                .push( min.getValue( mc ) );
                break;
			default:
				throw new RippleException( "unhandled operator type: " + type );	
		}

		sink.put( l );
	}

	public String toString()
	{
        String s = inverse ? "<<" : ">>";
        switch ( type )
		{
			case Apply:
				return s;
            case Option:
				return "?" + s;
            case Plus:
                return "+" + s;
            case Range:
                return "{" + min + "," + max + "}" + s;
            case Star:
				return "*" + s;
            case Times:
                return "{" + min + "}" + s;
			default:
				return "error";
		}
	}
}

