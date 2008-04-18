/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.impl.sesame.SesameNumericValue;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.test.RippleTestCase;

public class NumericLiteralTest extends RippleTestCase
{
	private class TypeTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			NumericValue
				intLit = new SesameNumericValue( 5 ),
				doubleLit = new SesameNumericValue( 3.1415926 );

			assertEquals( intLit.getType(), NumericValue.NumericLiteralType.INTEGER );
			assertEquals( doubleLit.getType(), NumericValue.NumericLiteralType.DOUBLE );

			assertEquals(
				intLit.abs().getType(),
				NumericValue.NumericLiteralType.INTEGER );
			assertEquals(
				doubleLit.abs().getType(),
				NumericValue.NumericLiteralType.DOUBLE );

			/*
			assertEquals(
				NumericLiteral.neg( intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.neg( doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.add( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.add( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.add( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.add( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.sub( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.sub( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.sub( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.sub( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.mul( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.mul( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.mul( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.mul( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.div( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.div( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.div( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.div( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.mod( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.mod( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.mod( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.mod( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );

			assertEquals(
				NumericLiteral.pow( intLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.INTEGER );
			assertEquals(
				NumericLiteral.pow( intLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.pow( doubleLit, intLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE );
			assertEquals(
				NumericLiteral.pow( doubleLit, doubleLit ).getType(),
				NumericLiteral.NumericLiteralType.DOUBLE ); */
		}
	}
	
	private class EqualityTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			// int == int
			assertEquals( new SesameNumericValue( 1 ), new SesameNumericValue( 1 ) );
			assertEquals( new SesameNumericValue( -1 ), new SesameNumericValue( -1 ) );
			assertEquals( new SesameNumericValue( 0 ), new SesameNumericValue( 0 ) );
			
			// long == long
			assertEquals( new SesameNumericValue( 1l ), new SesameNumericValue( 1l ) );
			assertEquals( new SesameNumericValue( -1l ), new SesameNumericValue( -1l ) );
			assertEquals( new SesameNumericValue( 0l ), new SesameNumericValue( 0l ) );
			
			// double == double
			assertEquals( new SesameNumericValue( 1.0 ), new SesameNumericValue( 1.0 ) );
			assertEquals( new SesameNumericValue( -1.0 ), new SesameNumericValue( -1.0 ) );
			assertEquals( new SesameNumericValue( 0.0 ), new SesameNumericValue( 0.0 ) );
			
			// mixed comparisons
			assertEquals( new SesameNumericValue( 1 ), new SesameNumericValue( 1l ) );
			assertEquals( new SesameNumericValue( 1 ), new SesameNumericValue( 1.0 ) );
			assertEquals( new SesameNumericValue( 1l ), new SesameNumericValue( 1 ) );
			assertEquals( new SesameNumericValue( 1l ), new SesameNumericValue( 1.0 ) );
			assertEquals( new SesameNumericValue( 1.0 ), new SesameNumericValue( 1 ) );
			assertEquals( new SesameNumericValue( 1.0 ), new SesameNumericValue( 1l ) );			
		}
	}

	public void runTests()
		throws Exception
	{
		testAsynchronous( new TypeTest() );
		testAsynchronous( new EqualityTest() );
	}
}

// kate: tab-width 4
