/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import java.util.Iterator;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

public class TurtleView implements Sink<RippleList, RippleException>
{
	// A three-space-INDENTed tree seems to be the most readable.
	private static final String INDENT = "   ";

	private static final String INDEX_SEPARATOR = "  ";

	private RipplePrintStream ps;
	private ModelConnection mc;
	private int index = 0;

    private boolean printEntireStack;
    private boolean showEdges;
    private int maxPredicates;
    private int maxObjects;

    public TurtleView( final RipplePrintStream printStream,
						final ModelConnection mc )
		throws RippleException
	{
		ps = printStream;
		this.mc = mc;

        RippleProperties props = Ripple.getProperties();
        this.printEntireStack = props.getBoolean(
                Ripple.RESULT_VIEW_PRINT_ENTIRE_STACK );
        this.showEdges = props.getBoolean(
                Ripple.RESOURCE_VIEW_SHOW_EDGES );
        this.maxPredicates = props.getInt(
                Ripple.RESULT_VIEW_MAX_PREDICATES );
        this.maxObjects = props.getInt(
                Ripple.RESULT_VIEW_MAX_OBJECTS );
    }

	public int size()
	{
		return index;
	}

	// Note: don't give this method a nil list.
	public void put( final RippleList stack ) throws RippleException
	{
		// Grab the topmost item on the stack.
		RippleValue first = stack.getFirst();

		// View the list in right-to-left order
		RippleList list = mc.invert( stack );

		ps.print( "rdf:_" + ++index + INDEX_SEPARATOR );
		ps.print( printEntireStack ? list : first );
		ps.print( "\n" );

		if ( showEdges )
		{
			Collector<RippleValue, RippleException> predicates = new Collector<RippleValue, RippleException>();
			mc.findPredicates( first, predicates );
	
			int predCount = 0;

			for ( Iterator<RippleValue> predIter = predicates.iterator();
				predIter.hasNext(); )
			{
				ps.print( INDENT );
	
				// Stop after maxPredicates predicates have been displayed, unless
				// maxPredicates < 0, which indicates an unlimited number of
				// predicates.
				if ( maxPredicates >= 0 && ++predCount > maxPredicates )
				{
					ps.print( "[...]\n" );
					break;
				}
	
				RippleValue predicate = predIter.next();
				ps.print( predicate );
				ps.print( "\n" );
	
				Collector<RippleValue, RippleException> objects = new Collector<RippleValue, RippleException>();
				mc.multiply( first, predicate, objects, Ripple.useInference() );
				
				int objCount = 0;
	
				for ( Iterator<RippleValue> objIter = objects.iterator();
					objIter.hasNext(); )
				{
					ps.print( INDENT );
					ps.print( INDENT );
	
					// Stop after maxObjects objects have been displayed, unless
					// maxObjects < 0, which indicates an unlimited number of
					// objects.
					if ( maxObjects >= 0 && ++objCount > maxObjects )
					{
						ps.print( "[...]\n" );
						break;
					}
	
					RippleValue object = objIter.next();
					ps.print( object );
					ps.print( ( objIter.hasNext() )
						? ","
						: ( predIter.hasNext() )
							? ";" : "." );
					ps.print( "\n" );
				}
			}
		}
	}
}

// kate: tab-width 4
