package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DoubleAST extends NumberAST {
    /*
    [Definition:]  The double datatype is patterned after the IEEE
    double-precision 64-bit floating point type [IEEE 754-1985]. The basic
    value space of double consists of the values m ? 2^e, where m is an
    integer whose absolute value is less than 2^53, and e is an integer
    between -1075 and 970, inclusive. In addition to the basic value space
    described above, the value space of double also contains the following
    three special values: positive and negative infinity and not-a-number (NaN).
    */
    private static final Pattern
            // TODO: apparently, digits after the decimal point are not required,
            // but are digits before the decimal point required?
            XSD_DOUBLE = Pattern.compile("NaN|INF|-INF|([-+]?\\d+([.]\\d*)?([eE][-+]?\\d+)?)");

    private static final BigDecimal
            SUP_MANTISSA = new BigDecimal(0x20000000000000l),  // 2^53
            MIN_EXPONENT = new BigDecimal(-1075l),
            MAX_EXPONENT = new BigDecimal(970l);

    private final double value;

    public DoubleAST(final double value) {
        this.value = value;
    }

    /**
     * @param rep the string representation of an xsd:double value
     */
    public DoubleAST(final String rep) {
        if (!XSD_DOUBLE.matcher(rep).matches()) {
            throw new IllegalArgumentException("invalid xsd:double value: " + rep);
        }

        if (rep.equals("NaN")) {
            value = Double.NaN;
        } else if (rep.equals("INF")) {
            value = Double.POSITIVE_INFINITY;
        } else if (rep.equals("-INF")) {
            value = Double.NEGATIVE_INFINITY;
        } else {
            String s = canonicalize(rep);

            // TODO: what happens when the number represented by the mantissa
            // or exponent portion of the number is larger than Long.MAX_VALUE?
            BigDecimal mantissa, exponent;
            int i = s.indexOf("e");

            // Exponent is omitted, assumed to be 0.
            if (-1 == i) {
                mantissa = new BigDecimal(s);
                exponent = BigDecimal.ZERO;
            }

            // Exponent is given.
            else {
                mantissa = new BigDecimal(s.substring(0, i));
                exponent = new BigDecimal(s.substring(1 + i));
            }

            if (0 <= mantissa.abs().compareTo(SUP_MANTISSA)) {
                throw new IllegalArgumentException("mantissa of xsd:double number is out of range: " + rep);
            }

            if (0 > exponent.compareTo(MIN_EXPONENT)
                    || 0 < exponent.compareTo(MAX_EXPONENT)) {
                throw new IllegalArgumentException("exponent of xsd:double number is out of range: " + rep);
            }

//System.out.println("mantissa = " + mantissa + ", exponent = " + exponent);
            value = mantissa.doubleValue() * Math.pow(10, exponent.doubleValue());
        }
    }

    public Number getValue(final ModelConnection mc) throws RippleException {
        return value;
    }

    public String toString() {
        // Note: these special values are not recognized by the Ripple
        // interpreter.  They must be checked for elsewhere.
        if (Double.NaN == value
                || Double.NEGATIVE_INFINITY == value
                || Double.POSITIVE_INFINITY == value) {
            return "" + value;
        } else {
            String s = "" + value;

            // Add an exponent, if necessary, to make this an unambiguous
            // xsd:double value in Ripple syntax.
            if (!s.contains("E")) {
                s += "E0";
            }

            // Note: this is not necessarily the canonical form.
            return s;
        }
    }
}

