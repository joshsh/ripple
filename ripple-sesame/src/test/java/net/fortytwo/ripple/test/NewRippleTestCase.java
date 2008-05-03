package net.fortytwo.ripple.test;

import junit.framework.TestCase;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;
import net.fortytwo.ripple.query.LazyEvaluator;
import net.fortytwo.ripple.query.QueryPipe;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.linkeddata.sail.LinkedDataSail;

import java.util.Iterator;
import java.util.Arrays;

import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:43:08 PM
 */
public abstract class NewRippleTestCase extends TestCase
{
	private static Sail sail = null;
	private static URIMap uriMap = null;
    private static Model model = null;
    private static QueryEngine queryEngine = null;

    private static Sail getTestSail() throws Exception
	{
		if ( null == sail )
		{
			// TODO: add a shutdown hook for this Sail
            sail = new MemoryStore();
            sail.initialize();
		}

		return sail;
	}

    private static URIMap getTestUriMap() throws Exception
    {
        if ( null == uriMap )
        {
            uriMap = new URIMap();
        }

        return uriMap;
    }

	protected static Model getTestModel() throws Exception
	{
		if ( null == model )
		{
            Ripple.initialize();

            // TODO: add a shutdown hook for this Model
            model = new SesameModel( getTestSail(), getTestUriMap() );
		}

		return model;
	}

    protected static QueryEngine getTestQueryEngine() throws Exception
    {
        if ( null == queryEngine )
        {
			StackEvaluator eval = new LazyEvaluator();
			queryEngine = new QueryEngine( getTestModel(), eval, System.out, System.err );
        }

        return queryEngine;
    }

    protected RippleList createStack( final ModelConnection mc,
                                          final RippleValue... values ) throws RippleException
	{
		if ( 0 == values.length )
		{
			return mc.list();
		}

		RippleList l = mc.list( values[0] );
		for ( int i = 1; i < values.length; i++ )
		{
			l = l.push( values[i] );
		}

		return l;
	}

	protected RippleList createQueue( final ModelConnection mc,
                                          final RippleValue... values ) throws RippleException
	{
		return createStack( mc, values ).invert();
	}

	protected void assertCollectorsEqual( final Collector<RippleList, RippleException> expected,
                                              final Collector<RippleList, RippleException> actual ) throws Exception
	{
//System.out.println("expected: " + expected + ", actual = " + actual);
        int size = expected.size();
		assertEquals( size, actual.size() );
		if ( 0 == size )
		{
			return;
		}

		RippleList[] expArray = new RippleList[size];
		RippleList[] actArray = new RippleList[size];
		Iterator<RippleList> expIter = expected.iterator();
		Iterator<RippleList> actIter = actual.iterator();
		for ( int i = 0; i < size; i++ )
		{
			expArray[i] = expIter.next();
			actArray[i] = actIter.next();
		}

		Arrays.sort( expArray );
		Arrays.sort( actArray );
		for ( int i = 0; i < size; i++ )
		{
//System.out.println("expected: " + expArray[i] + ", actual = " + actArray[i]);
			assertEquals( expArray[i], actArray[i] );
		}
	}
    
    protected void assertReducesTo( final String from, final String... to ) throws Exception
    {
        Collector<RippleList, RippleException>
                expected = new Collector<RippleList, RippleException>(),
                actual = new Collector<RippleList, RippleException>();

        QueryEngine qe = getTestQueryEngine();

        QueryPipe actualPipe = new QueryPipe( qe, actual );
        actualPipe.put( from + "." );
        actualPipe.close();

        QueryPipe expectedPipe = new QueryPipe( qe, expected );
        for ( String t : to )
        {
            expectedPipe.put( t + "." );
        }
        expectedPipe.close();

        assertCollectorsEqual( expected, actual );
//System.out.println( "########## expected.size() = " + expected.size() );
    }
}
