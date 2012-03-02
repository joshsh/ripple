/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.ListNode;

// TODO: this class has more plumbing than it needs
public class ListAST extends ListNode<AST> implements AST<RippleList>
{
	protected AST first;
	protected ListAST rest;

	/**
	 * Constructs a nil list AST.
	 */
	public ListAST()
	{
//System.out.println( "nil ListAst" );
		first = null;
		rest = null;
	}

	/**
	 * Constructs a list AST with the given first element and rest.
	 */
	public ListAST( final AST first, final ListAST rest )
	{
//System.out.println( "first = " + first + ", rest = " + rest );
		this.first = first;
		this.rest = rest;
	}

	public AST getFirst()
	{
		return first;
	}

	public ListAST getRest()
	{
		return rest;
	}

    public ListAST push( final AST a )
    {
        return new ListAST( a, this  );
    }

    public ListAST invert()
	{
        if ( this.isNil() )
        {
            return this;
        }

        else
        {
            ListAST in = this;
            ListAST out = new ListAST();

            while ( !in.isNil() )
            {
                out = new ListAST( in.getFirst(), out );
                in = in.getRest();
            }

            return out;
        }
    }

    public boolean isNil() {
        return null == getFirst();
    }

	public void evaluate( final Sink<RippleList> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put(final RippleList l) throws RippleException
			{
				sink.put( mc.list().push( l ) );
			}
		};

		createLists( this, listSink, qe, mc );
	}

	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append( "(" );

		ListNode<AST> cur = this;
		boolean first = true;
		while ( !cur.isNil() )
		{
			if ( first )
			{
				first = false;
			}

			else
			{
				s.append( " " );
			}

			s.append( cur.getFirst().toString() );
			cur = cur.getRest();
		}

		s.append( ")" );
		return s.toString();
	}

	private void createLists( final ListNode<AST> astList,
							final Sink<RippleList> sink,
							final QueryEngine qe,
							final ModelConnection mc )
		throws RippleException
	{
		if ( astList.isNil() )
		{
			sink.put( mc.list() );
		}

		else
		{
			final Collector<RippleList> firstValues = new Collector<RippleList>();
			astList.getFirst().evaluate( firstValues, qe, mc );
	
			Sink<RippleList> restSink = new Sink<RippleList>()
			{
				public void put( final RippleList rest ) throws RippleException
				{
					Sink<RippleList> firstSink = new Sink<RippleList>()
					{
						public void put( final RippleList f ) throws RippleException
						{
							sink.put( f.concat( rest ) );
						}
					};

					firstValues.writeTo( firstSink );
				}
			};

			createLists( astList.getRest(), restSink, qe, mc );
		}
	}
}

