/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StatementPatternQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A <code>Sink</code> which accepts Ripple stacks and prints them to a
 * specified stream in LIFO order, including a Turtle-styled tree view for the
 * topmost item in the stack.
 */
public class TurtleView implements Sink<RippleList, RippleException>
{
	// A three-space-indented tree seems to be the most readable.
	private static final String INDENT = "   ";

	private static final String INDEX_SEPARATOR = "  ";

	private final RipplePrintStream printStream;
	private final ModelConnection modelConnection;

    private final boolean printEntireStack;
    private final boolean showEdges;
    private final int maxPredicates;
    private final int maxObjects;
    private final boolean deduplicateObjects;

    private int index = 0;
    
    public TurtleView( final RipplePrintStream printStream,
						final ModelConnection modelConnection)
		throws RippleException
	{
		this.printStream = printStream;
		this.modelConnection = modelConnection;

        RippleProperties props = Ripple.getProperties();
        this.printEntireStack = props.getBoolean(
                Ripple.RESULT_VIEW_PRINT_ENTIRE_STACK );
        this.showEdges = props.getBoolean(
                Ripple.RESOURCE_VIEW_SHOW_EDGES );
        this.maxPredicates = props.getInt(
                Ripple.RESULT_VIEW_MAX_PREDICATES );
        this.maxObjects = props.getInt(
                Ripple.RESULT_VIEW_MAX_OBJECTS );
        this.deduplicateObjects = props.getBoolean(
                Ripple.RESULT_VIEW_DEDUPLICATE_OBJECTS );
    }

	public int size()
	{
		return index;
	}

    /**
     * Accepts a stack, printing it to the specified stream.
     * @param stack  the stack to print.  May not be a nil list.
     * @throws RippleException  if something goes awry
     */
    public void put( final RippleList stack ) throws RippleException
	{
		// Grab the topmost item on the stack.
		RippleValue subject = stack.getFirst();

		// View the list in right-to-left order
		RippleList list = stack.invert();

        String prefix = "  [" + ++index + "]" + INDEX_SEPARATOR;
        String prefixIndent = "                ".substring( 0, prefix.length() );
        printStream.print( prefix );
        //printStream.print( "" + ++index + " ->" + INDEX_SEPARATOR );
        //printStream.print( "rdf:_" + ++index + INDEX_SEPARATOR );

        if ( printEntireStack )
        {
            list.printTo( printStream, false );
        }

        else
        {
            subject.printTo( printStream );
        }

		printStream.print( "\n" );

		if ( showEdges )
		{
			Collector<RippleValue, RippleException> predicates = new Collector<RippleValue, RippleException>();
			modelConnection.findPredicates( subject, predicates );
	
			int predCount = 0;

			for ( Iterator<RippleValue> predIter = predicates.iterator();
				predIter.hasNext(); )
			{
                printStream.print( prefixIndent );
                printStream.print( INDENT );
	
				// Stop after maxPredicates predicates have been displayed, unless
				// maxPredicates < 0, which indicates an unlimited number of
				// predicates.
				if ( maxPredicates >= 0 && ++predCount > maxPredicates )
				{
					printStream.print( "[...]\n" );
					break;
				}
	
				RippleValue predicate = predIter.next();
				printStream.print( predicate );
				printStream.print( "\n" );
	
				Collector<RippleValue, RippleException> objects = new Collector<RippleValue, RippleException>();
                StatementPatternQuery query = new StatementPatternQuery( subject, predicate, null, Ripple.useInference() );
                modelConnection.query( query, objects );

				int objCount = 0;

                Collection<RippleValue> objColl;
                if ( deduplicateObjects )
                {
                    objColl = new HashSet<RippleValue>();
                    objColl.addAll( objects );
                }

                else
                {
                    objColl = objects;
                }

                for ( Iterator<RippleValue> objIter = objColl.iterator();
					objIter.hasNext(); )
				{
                    printStream.print( prefixIndent );
                    printStream.print( INDENT );
					printStream.print( INDENT );
	
					// Stop after maxObjects objects have been displayed, unless
					// maxObjects < 0, which indicates an unlimited number of
					// objects.
					if ( maxObjects >= 0 && ++objCount > maxObjects )
					{
						printStream.print( "[...]\n" );
						break;
					}
	
					RippleValue object = objIter.next();
					printStream.print( object );
					printStream.print( ( objIter.hasNext() )
						? ","
						: ( predIter.hasNext() )
							? ";" : "." );
					printStream.print( "\n" );
				}
			}
		}
	}
}
