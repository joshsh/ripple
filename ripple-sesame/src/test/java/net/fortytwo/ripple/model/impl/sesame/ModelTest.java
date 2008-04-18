/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.fortytwo.ripple.model.impl.sesame.SesameNumericValue;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.FileUtils;

public class ModelTest extends RippleTestCase
{
	private class ModelConstructorTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			getTestModel();
		}
	}
	
	private class NumericLiteralTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			Model model = getTestModel();
			ModelConnection mc = model.getConnection( "for NumericLiteralTest" );
			NumericValue l;
			
			// Create an integer literal.
			l = mc.value( 42 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.INTEGER, l.getType() );
			assertEquals( 42, l.intValue() );
			l = mc.value( 0 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.INTEGER, l.getType() );
			assertEquals( 0, l.intValue() );
			l = mc.value( -42 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.INTEGER, l.getType() );
			assertEquals( -42, l.intValue() );

			// Create a long literal.
			l = mc.value( 42l );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.LONG, l.getType() );
			assertEquals( 42l, l.longValue() );
			l = mc.value( 0l );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.LONG, l.getType() );
			assertEquals( 0l, l.longValue() );
			l = mc.value( -42l );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.LONG, l.getType() );
			assertEquals( -42l, l.longValue() );
			
			// Create a double literal
			l = mc.value( 42.0 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.DOUBLE, l.getType() );
			assertEquals( 42.0, l.doubleValue() );
			l = mc.value( 0.0 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.DOUBLE, l.getType() );
			assertEquals( 0.0, l.doubleValue() );
			l = mc.value( -42.0 );
			assertTrue( l instanceof NumericValue );
			assertEquals( NumericValue.NumericLiteralType.DOUBLE, l.getType() );
			assertEquals( -42.0, l.doubleValue() );
			
			InputStream is = ModelTest.class.getResourceAsStream( "numericLiteralTest.txt" );
			Iterator<String> lines = FileUtils.getLines( is ).iterator();
			is.close();
			Map<String, Integer> argsForFunc = new HashMap<String, Integer>();
			argsForFunc.put( "abs", new Integer( 1 ) );
			argsForFunc.put( "neg", new Integer( 1 ) );
			argsForFunc.put( "add", new Integer( 2 ) );
			argsForFunc.put( "sub", new Integer( 2 ) );
			argsForFunc.put( "mul", new Integer( 2 ) );
			argsForFunc.put( "div", new Integer( 2 ) );
			argsForFunc.put( "mod", new Integer( 2 ) );
			argsForFunc.put( "pow", new Integer( 2 ) );
			
			// Verify individual operator test cases.
			while ( lines.hasNext() )
			{
				StringTokenizer tokenizer = new StringTokenizer(
						lines.next(), " \t" );
				String func = tokenizer.nextToken();
				String signature = func + "(";
				int argv = argsForFunc.get( func );
				NumericValue[] args = new NumericValue[argv];
				for ( int i = 0; i < argv; i++)
				{
					String s = tokenizer.nextToken();
					if ( i > 0 )
					{
						signature += ", ";
					}
					signature += s;
					args[i] = createNumericLiteral( s );
				}
				signature += ")";
				
				// Skip the '=' token
				tokenizer.nextToken();
				
				NumericValue correctResult = createNumericLiteral( tokenizer.nextToken() );
				NumericValue actualResult = null;

				Throwable thrown = null;
				
				try
				{
					if ( func.equals( "abs" ) )
					{
						actualResult = args[0].abs();
					}

					else if ( func.equals( "neg" ) )
					{
						actualResult = args[0].neg();
					}

					else if ( func.equals( "add" ) )
					{
						actualResult = args[0].add( args[1] );
					}

					else if ( func.equals( "sub" ) )
					{
						actualResult = args[0].sub( args[1] );
					}

					else if ( func.equals( "mul" ) )
					{
						actualResult = args[0].mul( args[1] );
					}

					else if ( func.equals( "div" ) )
					{
						actualResult = args[0].div( args[1] );
					}

					else if ( func.equals( "mod" ) )
					{
						actualResult = args[0].mod( args[1] );
					}

					else if ( func.equals( "pow" ) )
					{
						actualResult = args[0].pow( args[1] );
					}

					else
					{
						throw new Exception( "bad function: " + func );
					}
				}
				
				catch ( Throwable t )
				{
					thrown = t;
				}
				
				if ( null == thrown )
				{
					assertTrue( "for case " + signature, null != correctResult );
					
					switch ( correctResult.getType() )
					{
					case INTEGER:
						assertEquals( "for case " + signature, NumericValue.NumericLiteralType.INTEGER, actualResult.getType() );
						assertEquals( "for case " + signature, correctResult.intValue(), actualResult.intValue() );
						break;
					case LONG:
						assertEquals( "for case " + signature, NumericValue.NumericLiteralType.LONG, actualResult.getType() );
						assertEquals( "for case " + signature, correctResult.longValue(), actualResult.longValue() );
						break;
					case DOUBLE:
						assertEquals( "for case " + signature, NumericValue.NumericLiteralType.DOUBLE, actualResult.getType() );
						assertEquals( "for case " + signature, correctResult.longValue(), actualResult.longValue() );
						break;
					}
				}
				
				else
				{
					if ( null != correctResult )
					{
						throw new Exception( "for case " + signature, thrown );
					}
				}
			}
			
// TODO: test NumericLiteral/RDF translation
			
			mc.close();
		}
		
		private NumericValue createNumericLiteral( final String s )
		{
			NumericValue l;
			
			if ( s.equals( "error" ) )
			{
				l = null;
			}
			
			else if ( s.equals( "infinity") )
			{
				l = new SesameNumericValue( Double.POSITIVE_INFINITY );
			}
			
			else if ( s.contains( "l" ) )
			{
				l = new SesameNumericValue( new Long( s.substring( 0, s.length() - 1 ) ).longValue() );
			}
			
			else if ( s.contains( "." ) )
			{
				l = new SesameNumericValue( new Double( s ).doubleValue() );
			}
			
			else
			{
				l = new SesameNumericValue( new Integer( s ).intValue() );
			}
			
			return l;
		}
	}
	
	public void runTests()
		throws Exception
	{
		testAsynchronous( new ModelConstructorTest() );
		testAsynchronous( new NumericLiteralTest() );
	}
}

// kate: tab-width 4
