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

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NumericValueTest extends RippleTestCase
{
    public void testValues() throws Exception
    {
        Model model = getTestModel();
        ModelConnection mc = model.createConnection();
        NumericValue l;

        // Create an integer literal.
        l = mc.numericValue(42);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getDatatype() );
        assertEquals( 42, l.intValue() );
        l = mc.numericValue(0);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getDatatype() );
        assertEquals( 0, l.intValue() );
        l = mc.numericValue(-42);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.INTEGER, l.getDatatype() );
        assertEquals( -42, l.intValue() );

        // Create a long literal.
        l = mc.numericValue(42l);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getDatatype() );
        assertEquals( 42l, l.longValue() );
        l = mc.numericValue(0l);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getDatatype() );
        assertEquals( 0l, l.longValue() );
        l = mc.numericValue(-42l);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.LONG, l.getDatatype() );
        assertEquals( -42l, l.longValue() );

        // Create a double literal
        l = mc.numericValue(42.0);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getDatatype() );
        assertEquals( 42.0, l.doubleValue() );
        l = mc.numericValue(0.0);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getDatatype() );
        assertEquals( 0.0, l.doubleValue() );
        l = mc.numericValue(-42.0);
        assertTrue( l instanceof NumericValue );
        assertEquals( NumericValue.Type.DOUBLE, l.getDatatype() );
        assertEquals( -42.0, l.doubleValue() );

        InputStream is = ModelTest.class.getResourceAsStream( "numericLiteralTest.txt" );
        Iterator<String> lines = FileUtils.getLines( is ).iterator();
        is.close();
        Map<String, Integer> argsForFunc = new HashMap<String, Integer>();
        argsForFunc.put( "abs", 1);
        argsForFunc.put( "neg", 1);
        argsForFunc.put( "add", 2);
        argsForFunc.put( "sub", 2);
        argsForFunc.put( "mul", 2);
        argsForFunc.put( "div", 2);
        argsForFunc.put( "mod", 2);
        argsForFunc.put( "pow", 2);

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

                switch ( correctResult.getDatatype() )
                {
                case INTEGER:
                    assertEquals( "for case " + signature, NumericValue.Type.INTEGER, actualResult.getDatatype() );
                    assertEquals( "for case " + signature, correctResult.intValue(), actualResult.intValue() );
                    break;
                case LONG:
                    assertEquals( "for case " + signature, NumericValue.Type.LONG, actualResult.getDatatype() );
                    assertEquals( "for case " + signature, correctResult.longValue(), actualResult.longValue() );
                    break;
                case DOUBLE:
                    assertEquals( "for case " + signature, NumericValue.Type.DOUBLE, actualResult.getDatatype() );
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
            l = new SesameNumericValue(new Long(s.substring(0, s.length() - 1)));
        }

        else if ( s.contains( "." ) )
        {
            l = new SesameNumericValue(new Double(s));
        }

        else
        {
            l = new SesameNumericValue(new Integer(s));
        }

        return l;
    }

    public void testTypes()
        throws Exception
    {
        NumericValue
            intLit = new SesameNumericValue( 5 ),
            doubleLit = new SesameNumericValue( 3.1415926 );

        assertEquals( intLit.getDatatype(), NumericValue.Type.INTEGER );
        assertEquals( doubleLit.getDatatype(), NumericValue.Type.DOUBLE );

        assertEquals(
            intLit.abs().getDatatype(),
            NumericValue.Type.INTEGER );
        assertEquals(
            doubleLit.abs().getDatatype(),
            NumericValue.Type.DOUBLE );

        /*
        assertEquals(
            NumericLiteral.neg( intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.neg( doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.add( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.add( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.add( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.add( doubleLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.sub( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.sub( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.sub( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.sub( doubleLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.mul( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.mul( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.mul( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.mul( doubleLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.div( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.div( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.div( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.div( doubleLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.mod( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.mod( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.mod( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.mod( doubleLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );

        assertEquals(
            NumericLiteral.pow( intLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.INTEGER );
        assertEquals(
            NumericLiteral.pow( intLit, doubleLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.pow( doubleLit, intLit ).getDatatype(),
            NumericLiteral.NumericLiteralType.DOUBLE );
        assertEquals(
            NumericLiteral.pow( doubleLit, doubleLit ).getDatatype(),
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

