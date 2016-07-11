package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.types.NumericType;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.FileUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NumericValueTest extends RippleTestCase {
    @Test
    public void testValues() throws Exception {
        Model model = getTestModel();
        ModelConnection mc = model.createConnection();
        Number l;

        // Create an integer literal.
        l = 42;
        assertEquals(NumericType.Datatype.INTEGER, NumericType.datatypeOf(l));
        assertEquals(42, l.intValue());
        l = 0;
        assertEquals(NumericType.Datatype.INTEGER, NumericType.datatypeOf(l));
        assertEquals(0, l.intValue());
        l = -42;
        assertEquals(NumericType.Datatype.INTEGER, NumericType.datatypeOf(l));
        assertEquals(-42, l.intValue());

        // Create a long literal.
        l = 42L;
        assertEquals(NumericType.Datatype.LONG, NumericType.datatypeOf(l));
        assertEquals(42l, l.longValue());
        l = 0l;
        assertEquals(NumericType.Datatype.LONG, NumericType.datatypeOf(l));
        assertEquals(0l, l.longValue());
        l = -42l;
        assertEquals(NumericType.Datatype.LONG, NumericType.datatypeOf(l));
        assertEquals(-42l, l.longValue());

        // Create a double literal
        l = 42.0;
        assertEquals(NumericType.Datatype.DOUBLE, NumericType.datatypeOf(l));
        assertEquals(42.0, l.doubleValue(), ASSERTEQUALS_EPSILON);
        l = 0.0;
        assertEquals(NumericType.Datatype.DOUBLE, NumericType.datatypeOf(l));
        assertEquals(0.0, l.doubleValue(), ASSERTEQUALS_EPSILON);
        l = -42.0;
        assertEquals(NumericType.Datatype.DOUBLE, NumericType.datatypeOf(l));
        assertEquals(-42.0, l.doubleValue(), ASSERTEQUALS_EPSILON);

        InputStream is = ModelTest.class.getResourceAsStream("numericLiteralTest.txt");
        Iterator<String> lines = FileUtils.getLines(is).iterator();
        is.close();
        Map<String, Integer> argsForFunc = new HashMap<>();
        argsForFunc.put("abs", 1);
        argsForFunc.put("neg", 1);
        argsForFunc.put("add", 2);
        argsForFunc.put("sub", 2);
        argsForFunc.put("mul", 2);
        argsForFunc.put("div", 2);
        argsForFunc.put("mod", 2);
        argsForFunc.put("pow", 2);

        // Verify individual operator test cases.
        while (lines.hasNext()) {
            StringTokenizer tokenizer = new StringTokenizer(
                    lines.next(), " \t");
            String func = tokenizer.nextToken();
            String signature = func + "(";
            int argv = argsForFunc.get(func);
            Number[] args = new Number[argv];
            for (int i = 0; i < argv; i++) {
                String s = tokenizer.nextToken();
                if (i > 0) {
                    signature += ", ";
                }
                signature += s;
                args[i] = createNumericLiteral(s);
            }
            signature += ")";

            // Skip the '=' token
            tokenizer.nextToken();

            Number correctResult = createNumericLiteral(tokenizer.nextToken());
            Number actualResult = null;

            Throwable thrown = null;

            try {
                switch (func) {
                    case "abs":
                        actualResult = NumericType.abs(args[0]);
                        break;
                    case "neg":
                        actualResult = NumericType.neg(args[0]);
                        break;
                    case "add":
                        actualResult = NumericType.add(args[0], args[1]);
                        break;
                    case "sub":
                        actualResult = NumericType.sub(args[0], args[1]);
                        break;
                    case "mul":
                        actualResult = NumericType.mul(args[0], args[1]);
                        break;
                    case "div":
                        actualResult = NumericType.div(args[0], args[1]);
                        break;
                    case "mod":
                        actualResult = NumericType.mod(args[0], args[1]);
                        break;
                    case "pow":
                        actualResult = NumericType.pow(args[0], args[1]);
                        break;
                    default:
                        throw new Exception("bad function: " + func);
                }
            } catch (Throwable t) {
                thrown = t;
            }

            if (null == thrown) {
                assertTrue("for case " + signature, null != correctResult);

                switch (NumericType.datatypeOf(correctResult)) {
                    case INTEGER:
                        assertEquals("for case " + signature, NumericType.Datatype.INTEGER, NumericType.datatypeOf(actualResult));
                        assertEquals("for case " + signature, correctResult.intValue(), actualResult.intValue());
                        break;
                    case LONG:
                        assertEquals("for case " + signature, NumericType.Datatype.LONG, NumericType.datatypeOf(actualResult));
                        assertEquals("for case " + signature, correctResult.longValue(), actualResult.longValue());
                        break;
                    case DOUBLE:
                        assertEquals("for case " + signature, NumericType.Datatype.DOUBLE, NumericType.datatypeOf(actualResult));
                        assertEquals("for case " + signature, correctResult.longValue(), actualResult.longValue());
                        break;
                }
            } else {
                if (null != correctResult) {
                    throw new Exception("for case " + signature, thrown);
                }
            }
        }

// TODO: test NumericLiteral/RDF translation

        mc.close();
    }

    private Number createNumericLiteral(final String s) {
        Number l;

        if (s.equals("error")) {
            l = null;
        } else if (s.equals("infinity")) {
            l = Double.POSITIVE_INFINITY;
        } else if (s.contains("l")) {
            l = new Long(s.substring(0, s.length() - 1));
        } else if (s.contains(".")) {
            l = new Double(s);
        } else {
            l = new Integer(s);
        }

        return l;
    }

    @Test
    public void testTypes() throws Exception {
        Number
                intLit = 5,
                doubleLit = 3.1415926;

        assertEquals(NumericType.Datatype.INTEGER, NumericType.datatypeOf(intLit));
        assertEquals(NumericType.Datatype.DOUBLE, NumericType.datatypeOf(doubleLit));

        assertEquals(NumericType.Datatype.INTEGER,
                NumericType.datatypeOf(NumericType.abs(intLit)));
        assertEquals(NumericType.Datatype.DOUBLE,
                NumericType.datatypeOf(NumericType.abs(doubleLit)));

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

    /*
    @Test
    public void testEquality()
            throws Exception {
        // int == int
        assertEquals(new SesameNumericValue(1), new SesameNumericValue(1));
        assertEquals(new SesameNumericValue(-1), new SesameNumericValue(-1));
        assertEquals(new SesameNumericValue(0), new SesameNumericValue(0));

        // long == long
        assertEquals(new SesameNumericValue(1l), new SesameNumericValue(1l));
        assertEquals(new SesameNumericValue(-1l), new SesameNumericValue(-1l));
        assertEquals(new SesameNumericValue(0l), new SesameNumericValue(0l));

        // double == double
        assertEquals(new SesameNumericValue(1.0), new SesameNumericValue(1.0));
        assertEquals(new SesameNumericValue(-1.0), new SesameNumericValue(-1.0));
        assertEquals(new SesameNumericValue(0.0), new SesameNumericValue(0.0));

        // decimal == decimal
        assertEquals(new SesameNumericValue(new BigDecimal(1.0)), new SesameNumericValue(new BigDecimal(1.0)));
        assertEquals(new SesameNumericValue(new BigDecimal(-1.0)), new SesameNumericValue(new BigDecimal(-1.0)));
        assertEquals(new SesameNumericValue(new BigDecimal(0.0)), new SesameNumericValue(new BigDecimal(0.0)));

        // mixed comparisons
        assertEquals(new SesameNumericValue(1), new SesameNumericValue(1l));
        assertEquals(new SesameNumericValue(1), new SesameNumericValue(1.0));
        assertEquals(new SesameNumericValue(1l), new SesameNumericValue(1));
        assertEquals(new SesameNumericValue(1l), new SesameNumericValue(1.0));
        assertEquals(new SesameNumericValue(1.0), new SesameNumericValue(1));
        assertEquals(new SesameNumericValue(1.0), new SesameNumericValue(1l));
        assertEquals(new SesameNumericValue(new BigDecimal(1.0)), new SesameNumericValue(1));
        assertEquals(new SesameNumericValue(new BigDecimal(1.0)), new SesameNumericValue(1l));
        assertEquals(new SesameNumericValue(new BigDecimal(1.0)), new SesameNumericValue(1.0));
    }
    */
}

