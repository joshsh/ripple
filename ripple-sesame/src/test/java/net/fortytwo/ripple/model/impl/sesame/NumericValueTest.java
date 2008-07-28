/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.FileUtils;

import java.math.BigDecimal;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class NumericValueTest extends RippleTestCase
{
    public void testValues() throws Exception
    {
        Model model = getTestModel();
        ModelConnection mc = model.getConnection( "for NumericLiteralTest" );
        NumericValue l;

        // Create an integer literal.
        l = mc.value( 42 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getType() );
        assertEquals( 42, l.intValue() );
        l = mc.value( 0 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getType() );
        assertEquals( 0, l.intValue() );
        l = mc.value( -42 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getType() );
        assertEquals( -42, l.intValue() );

        // Create a long literal.
        l = mc.value( 42l );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getType() );
        assertEquals( 42l, l.longValue() );
        l = mc.value( 0l );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getType() );
        assertEquals( 0l, l.longValue() );
        l = mc.value( -42l );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getType() );
        assertEquals( -42l, l.longValue() );

        // Create a double literal
        l = mc.value( 42.0 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getType() );
        assertEquals( 42.0, l.doubleValue() );
        l = mc.value( 0.0 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getType() );
        assertEquals( 0.0, l.doubleValue() );
        l = mc.value( -42.0 );
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getType() );
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
                    assertEquals( "for case " + signature, NumericValue.Type.INTEGER, actualResult.getType() );
                    assertEquals( "for case " + signature, correctResult.intValue(), actualResult.intValue() );
                    break;
                case LONG:
                    assertEquals( "for case " + signature, NumericValue.Type.LONG, actualResult.getType() );
                    assertEquals( "for case " + signature, correctResult.longValue(), actualResult.longValue() );
                    break;
                case DOUBLE:
                    assertEquals( "for case " + signature, NumericValue.Type.DOUBLE, actualResult.getType() );
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

    public void testTypes()
        throws Exception
    {
        NumericValue
            intLit = new SesameNumericValue( 5 ),
            doubleLit = new SesameNumericValue( 3.1415926 );

        assertEquals( intLit.getType(), NumericValue.Type.INTEGER );
        assertEquals( doubleLit.getType(), NumericValue.Type.DOUBLE );

        assertEquals(
            intLit.abs().getType(),
            NumericValue.Type.INTEGER );
        assertEquals(
            doubleLit.abs().getType(),
            NumericValue.Type.DOUBLE );

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

    public void testEquality()
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

        // decimal == decimal
        assertEquals( new SesameNumericValue( new BigDecimal( 1.0 ) ), new SesameNumericValue( new BigDecimal( 1.0 ) ) );
        assertEquals( new SesameNumericValue( new BigDecimal( -1.0 ) ), new SesameNumericValue( new BigDecimal (-1.0 ) ) );
        assertEquals( new SesameNumericValue( new BigDecimal( 0.0 ) ), new SesameNumericValue( new BigDecimal( 0.0 ) ) );

        // mixed comparisons
        assertEquals( new SesameNumericValue( 1 ), new SesameNumericValue( 1l ) );
        assertEquals( new SesameNumericValue( 1 ), new SesameNumericValue( 1.0 ) );
        assertEquals( new SesameNumericValue( 1l ), new SesameNumericValue( 1 ) );
        assertEquals( new SesameNumericValue( 1l ), new SesameNumericValue( 1.0 ) );
        assertEquals( new SesameNumericValue( 1.0 ), new SesameNumericValue( 1 ) );
        assertEquals( new SesameNumericValue( 1.0 ), new SesameNumericValue( 1l ) );
        assertEquals( new SesameNumericValue( new BigDecimal( 1.0 ) ), new SesameNumericValue( 1 ) );
        assertEquals( new SesameNumericValue( new BigDecimal( 1.0 ) ), new SesameNumericValue( 1l ) );
        assertEquals( new SesameNumericValue( new BigDecimal( 1.0 ) ), new SesameNumericValue( 1.0 ) );
    }
}

