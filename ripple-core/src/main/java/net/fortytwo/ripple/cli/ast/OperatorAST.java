/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
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
        Apply, InverseApply,
        Option, InverseOption,
        Star, InverseStar,
        Plus, InversePlus,
        Times, InverseTimes,
        Range, InverseRange };

	private Type type;
	private int min, max;

	public OperatorAST()
	{
		this( Type.Apply);
	}

	public OperatorAST( final Type type )
	{
		this.type = type;
	}

	public OperatorAST( final int times, boolean inverse )
	{
		type = inverse ? Type.InverseTimes : Type.Times;
		min = times;
	}

	public OperatorAST( final int min, final int max, boolean inverse )
	{
		type = inverse ? Type.InverseRange : Type.Range;
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
				l = mc.list( Operator.OP );
				break;
            case InverseApply:
                l = mc.list( Operator.OP ).push( Operator.OP ).push( EtcLibrary.getInvertValue() );
                break;
            case InverseOption:
                l = mc.list( Operator.OP ).push( StackLibrary.getOptApplyValue() ).push( Operator.OP ).push( EtcLibrary.getInvertValue() );
                break;
            case InversePlus:
                l = mc.list( Operator.OP ).push( StackLibrary.getPlusApplyValue() ).push( Operator.OP ).push( EtcLibrary.getInvertValue() );
                break;
            case InverseRange:
                l = mc.list( Operator.OP )
						.push( StackLibrary.getRangeApplyValue() )
						.push( mc.value( max ) )
						.push( mc.value( min ) ).push( EtcLibrary.getInvertValue() );
                break;
            case InverseStar:
                l = mc.list( Operator.OP ).push( StackLibrary.getStarApplyValue() ).push( Operator.OP ).push( EtcLibrary.getInvertValue() );
                break;
            case InverseTimes:
				l = mc.list( Operator.OP )
						.push( StackLibrary.getRangeApplyValue() )
						.push( mc.value( max ) )
						.push( mc.value( min ) ).push( EtcLibrary.getInvertValue() );
                break;
            case Option:
				l = mc.list( Operator.OP ).push( StackLibrary.getOptApplyValue() );
				break;
            case Plus:
                l = mc.list( Operator.OP ).push( StackLibrary.getPlusApplyValue() );
                break;
            case Range:
                l = mc.list( Operator.OP )
                        .push( StackLibrary.getRangeApplyValue() )
                        .push( mc.value( max ) )
                        .push( mc.value( min ) );
                break;
			case Star:
				l = mc.list( Operator.OP ).push( StackLibrary.getStarApplyValue() );
				break;
            case Times:
                l = mc.list( Operator.OP )
                        .push( StackLibrary.getTimesApplyValue() )
                        .push( mc.value( min ) );
                break;
			default:
				throw new RippleException( "unhandled operator type: " + type );	
		}

		sink.put( l );
	}

	public String toString()
	{
		switch ( type )
		{
			case Apply:
				return ">>";
            case InverseApply:
                return "<<";
            case InverseOption:
                return "<<?";
            case InverseStar:
                return "<<*";
            case InversePlus:
                return "<<+";
            case InverseRange:
                return "<<{" + min + "," + max + "}";
            case InverseTimes:
                return "<<{" + min + "}";
            case Option:
				return ">>?";
            case Plus:
                return ">>+";
            case Range:
                return ">>{" + min + "," + max + "}";
            case Star:
				return ">>*";
            case Times:
                return ">>{" + min + "}";
			default:
				return "error";
		}
	}
}

// kate: tab-width 4
