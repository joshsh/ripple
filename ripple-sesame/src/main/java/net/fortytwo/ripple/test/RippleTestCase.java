package net.fortytwo.ripple.test;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;
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

import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Comparator;
import java.io.InputStream;

import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:43:08 PM
 */
public abstract class RippleTestCase extends TestCase
{
    private static final boolean SUPPORT_INFERENCE = true;

    // TODO: add a shutdown hook to clean up these objects
    private static Sail sail = null;
	private static URIMap uriMap = null;
    private static Model model = null;
    private static QueryEngine queryEngine = null;

    protected ModelConnection modelConnection = null;
    protected Comparator<RippleValue> comparator = null;

    public void setUp() throws Exception
    {
        modelConnection = getTestModel().getConnection( null );
        comparator = modelConnection.getComparator();
    }

    public void tearDown() throws Exception
    {
        if ( null != modelConnection )
        {
            modelConnection.close();
            modelConnection = null;
        }
    }

    protected Sail getTestSail() throws RippleException
    {
		if ( null == sail )
		{
            sail = new MemoryStore();
System.out.println("sail = " + sail);

            if ( SUPPORT_INFERENCE )
            {
                sail = new ForwardChainingRDFSInferencer( sail );
            }

            try {
                sail.initialize();

                SailConnection sc = sail.getConnection();
                try
                {
                    // Define some common namespaces
                    sc.setNamespace( "rdf", RDF.NAMESPACE );
                    sc.setNamespace( "rdfs", RDFS.NAMESPACE );
                    sc.setNamespace( "xsd", XMLSchema.NAMESPACE );
                    sc.commit();
                }

                finally
                {
                    sc.close();
                }
            }

            catch ( SailException e ) {
                throw new RippleException( e );
            }
        }

		return sail;
	}

    protected URIMap getTestURIMap()
    {
        if ( null == uriMap )
        {
            uriMap = new URIMap();
        }

        return uriMap;
    }

	protected Model getTestModel() throws RippleException
    {
		if ( null == model )
		{
            Ripple.initialize();

            model = new SesameModel( getTestSail(), Ripple.class.getResource( "libraries.txt" ), getTestURIMap() );
        }

		return model;
	}

    protected QueryEngine getTestQueryEngine() throws RippleException
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

        // Sort the results.
        RippleList[] expArray = new RippleList[size];
		RippleList[] actArray = new RippleList[size];
		Iterator<RippleList> expIter = expected.iterator();
		Iterator<RippleList> actIter = actual.iterator();
		for ( int i = 0; i < size; i++ )
		{
			expArray[i] = expIter.next();
			actArray[i] = actIter.next();
		}
		Arrays.sort( expArray, comparator );
		Arrays.sort( actArray, comparator );
/*System.out.println("expected:");
for ( RippleList l : expArray )
{
    System.out.println("    " + l );
}
System.out.println("actual:");
for ( RippleList l : actArray )
{
    System.out.println("    " + l );
}*/

        // Compare the results by pairs.
        for ( int i = 0; i < size; i++ )
		{
//System.out.println("expected (" + expArray[i].getClass() + "): " + expArray[i] + ", actual (" + actArray[i].getClass() + "): " + actArray[i]);
/*RippleList l;
l = expArray[i];
System.out.println("expected: (" + l.getClass() + ") -- " + l);
while (!l.isNil()) {
    RippleValue f = l.getFirst();
    System.out.println("    (" + f.getClass() + ") -- " + f);
    l = l.getRest();
}
l = actArray[i];
System.out.println("actual: (" + l.getClass() + ") -- " + l);
while (!l.isNil()) {
    RippleValue f = l.getFirst();
    System.out.println("    (" + f.getClass() + ") -- " + f);
    l = l.getRest();
}*/
            assertEquals( expArray[i], actArray[i] );
		}
	}

    protected void assertEquals( final RippleValue first, final RippleValue second ) throws Exception
    {
        int cmp = comparator.compare( first, second );
        if ( 0 != cmp )
        {
            throw new AssertionFailedError( "expected <" + first + "> but was <" + second + ">" );
        }
    }

    protected Collection<RippleList> reduce( final InputStream from ) throws RippleException
    {
        Collector<RippleList, RippleException>
                results = new Collector<RippleList, RippleException>();

        QueryEngine qe = getTestQueryEngine();

        QueryPipe actualPipe = new QueryPipe( qe, results );
        actualPipe.put( from );
        actualPipe.close();

        Collection<RippleList> c = new LinkedList<RippleList>();
        for ( Iterator<RippleList> iter = results.iterator(); iter.hasNext(); )
        {
            c.add( iter.next() );
        }

        return c;
    }

    protected Collection<RippleList> reduce( final String from ) throws RippleException
    {
        Collector<RippleList, RippleException>
                results = new Collector<RippleList, RippleException>();

        QueryEngine qe = getTestQueryEngine();

        QueryPipe actualPipe = new QueryPipe( qe, results );
        actualPipe.put( from + "." );
        actualPipe.close();

        Collection<RippleList> c = new LinkedList<RippleList>();
        for ( Iterator<RippleList> iter = results.iterator(); iter.hasNext(); )
        {
            c.add( iter.next() );
        }
        
        return c;
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
