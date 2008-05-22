/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Collector;

import java.util.Iterator;

/**
 * A primitive which consumes a list and a mapping, then applies the mapping to
 * each element in the list (as if it were on top of the remainder of the stack)
 * in turn, yielding another list.
 * For instance, <code>(1 2 3) (10 add >>) map >></code> yields <code>(11 12 13)</code>
 */
public class Map extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_06 + "map"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Map() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		final RippleValue mappingVal = stack.getFirst();
		stack = stack.getRest();
		RippleValue listVal = stack.getFirst();
		final RippleList rest = stack.getRest();

        // Note: it is simply assumed that these mappings have a production of
        // exactly one item.
        final Collector<Operator, RippleException> operators = new Collector<Operator, RippleException>();
        Operator.createOperator( mappingVal, operators, mc );

        Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
            public void put( final RippleList list ) throws RippleException
			{
                if ( list.isNil() )
                {
                    sink.put( arg.with( rest.push( list ) ) );
                }

                // TODO: this is probably a little more complicated than it needs to be
                else
                {
                    RippleList inverted = list.invert();
                    RippleValue f = inverted.getFirst();

                    for (Iterator<Operator> iter = operators.iterator(); iter.hasNext(); )
                    {
                        StackMapping inner = new InnerMapping( mc.list(), inverted.getRest(), iter.next() );
                        sink.put( arg.with( rest.push( f ).push( mappingVal ).push( Operator.OP ).push( new Operator( inner ) ) ) );
                    }
                }
            }
		};

		mc.toList( listVal, listSink );
    }

    private class InnerMapping implements StackMapping
    {
        private RippleList invertedListHead;
        private RippleList constructedList;
        private Operator operator;

        public InnerMapping( final RippleList constructedList, final RippleList invertedListHead, final Operator operator )
        {
            this.constructedList = constructedList;
            this.invertedListHead = invertedListHead;
            this.operator = operator;
        }

        public int arity()
        {
            return 1;
        }

        public StackMapping inverse() throws RippleException
        {
            return new NullStackMapping();
        }

        public boolean isTransparent() {
            return true;
        }

        public void applyTo( final StackContext arg, final Sink<StackContext, RippleException> sink ) throws RippleException
        {
            RippleList stack = arg.getStack();
            RippleValue first = stack.getFirst();
            stack = stack.getRest();

            RippleList newListRest = constructedList.push( first );

            if ( invertedListHead.isNil() )
            {
                sink.put( arg.with( stack.push( newListRest ) ) );
            }

            else
            {
                // The stack to operate on
                RippleList restStack = stack.push( invertedListHead.getFirst() ).push( operator );

                StackMapping restMapping = new InnerMapping( newListRest, invertedListHead.getRest(), operator );
                sink.put( arg.with( restStack.push( new Operator( restMapping ) ) ) );
            }
        }
    }
}