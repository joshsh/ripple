/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.RippleException;

import java.util.Random;
import java.io.PrintStream;

public class QueryPipeTest extends RippleTestCase
{
	private class QueriesTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			Model model = RippleTestCase.getTestModel();
			StackEvaluator eval = new LazyEvaluator();
			QueryEngine qe = new QueryEngine( model, eval, System.out, System.err );
			Collector<RippleList, RippleException> expected = new Collector<RippleList, RippleException>();
			Collector<RippleList, RippleException> results = new Collector<RippleList, RippleException>();
			QueryPipe qp = new QueryPipe( qe, results );
			ModelConnection mc = qe.getConnection();

			RippleValue
					zero = mc.value( 0 ),
					four = mc.value( 4 ),
					five = mc.value( 5 );
			
			// A simple expression.
			results.clear();
			qp.put( "2 3 add >> .\n" );
			expected.clear();
			expected.put( createStack( mc, five ) );
			assertCollectorsEqual( expected, results );

			// A slightly more complex expression.
			results.clear();
			qp.put( "105"
					+ " ((1 2 3 4 5) 0 add fold!) >>{7}"
					+ " add >>{6} sub >> .\n" );
			expected.clear();
			expected.put( createStack( mc, zero ) );
			assertCollectorsEqual( expected, results );
			
			// A branching expression.
			results.clear();
			qp.put( "(1 2) each >> 3 add >> .\n" );
			expected.clear();
			expected.put( createStack( mc, four ) );
			expected.put( createStack( mc, five ) );
			assertCollectorsEqual( expected, results );
			
			qp.close();
			mc.close();
		}
	}

	private class FuzzTest extends TestRunnable
	{
		private static final int
				REPEAT = 10,
				MIN_EXPR_LENGTH = 0,
				MAX_EXPR_LENGTH = 50;
		private Random rand = new Random();

		public void test() throws Exception
		{
			Model model = RippleTestCase.getTestModel();
			StackEvaluator eval = new LazyEvaluator();

			// Discard error output (of which there will be a lot).
			PrintStream errStream = new PrintStream( new NullOutputStream() );

			QueryEngine qe = new QueryEngine( model, eval, System.out, errStream );
			Collector<RippleList, RippleException> expected = new Collector<RippleList, RippleException>();
			Collector<RippleList, RippleException> results = new Collector<RippleList, RippleException>();
			QueryPipe qp = new QueryPipe( qe, results );
			ModelConnection mc = qe.getConnection();

			RippleValue
					five = mc.value( 5 );

			byte[] bytes = new byte[128];
			for ( int i = 0; i < bytes.length; i++ )
			{
				bytes[i] = ( i >= 32 )
						? (byte) i
						: ( i >= 16 )
								? (byte) '\n'
								: (byte) '\t';
			}

			for ( int i = 0; i < REPEAT; i++ )
			{
			 	// Feed the pipe some fuzz.
				int len = MAX_EXPR_LENGTH + rand.nextInt( MAX_EXPR_LENGTH - MIN_EXPR_LENGTH );
				byte[] expr = new byte[len];
				for ( int j = 0; j < len; j++ )
				{
					expr[j] = bytes[rand.nextInt(bytes.length)];
				}
				String s = new String( expr );
				qp.put( new String( expr ) );

				qp.put(".\n");
				qp.put(".\n");
				qp.put(".\n");

				// Now make sure the pipe still works.
				results.clear();
				qp.put( "2 3 add >> .\n" );
				expected.clear();
				expected.put( createStack( mc, five ) );
				assertCollectorsEqual( expected, results );
			}

			qp.close();
			mc.close();
		}
	}
	
	public void runTests()
		throws Exception
	{
		testSynchronous( new QueriesTest() );
		testSynchronous( new FuzzTest() );
	}
}
