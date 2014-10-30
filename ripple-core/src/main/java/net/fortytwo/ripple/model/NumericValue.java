package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.XMLSchema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A numeric (xsd:integer or xsd:double) literal value.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class NumericValue implements RippleValue, Comparable<NumericValue> {
    /**
     * Distinguishes between numeric literals of type xsd:integer and xsd:double.
     */
    public enum Type {
        INTEGER, LONG, DOUBLE, FLOAT, DECIMAL
    }

    protected static final Map<URI, Type> uriToTypeMap;

    static {
        uriToTypeMap = new HashMap<URI, Type>();
        uriToTypeMap.put(XMLSchema.INTEGER, Type.INTEGER);
        uriToTypeMap.put(XMLSchema.LONG, Type.LONG);
        uriToTypeMap.put(XMLSchema.DOUBLE, Type.DOUBLE);
        uriToTypeMap.put(XMLSchema.FLOAT, Type.FLOAT);
        uriToTypeMap.put(XMLSchema.DECIMAL, Type.DECIMAL);
    }

    protected Type type;
    protected Number number;

    // TODO: move into implementation
    protected RDFValue rdfEquivalent = null;

    public Type getDatatype() {
        return type;
    }

    protected Number getNumber() {
        return number;
    }

    public int intValue() {
        return number.intValue();
    }

    public long longValue() {
        return number.longValue();
    }

    public double doubleValue() {
        return number.doubleValue();
    }

    public float floatValue() {
        return number.floatValue();
    }

    public BigDecimal decimalValue() {
        return (number instanceof BigDecimal)
                ? (BigDecimal) number
                : BigDecimal.valueOf(number.doubleValue());
    }

    public boolean isZero() {
        return (0.0 == number.doubleValue());
    }

    public int sign() {
        double x = number.doubleValue();
        return x < 0.0 ? -1 : x > 0 ? 1 : 0;
    }

    ////////////////////////////////////////////////////////////////////////////

    public StackMapping getMapping() {
        return null;
    }

    public void printTo(final RipplePrintStream p)
            throws RippleException {
        switch (getDatatype()) {
            case INTEGER:
                p.printInteger(intValue());
                break;
            case LONG:
                p.printTypedLiteral(toString(), XMLSchema.LONG);
                break;
            case DOUBLE:
                p.printDouble(doubleValue());
                break;
            case FLOAT:
                p.printTypedLiteral(toString(), XMLSchema.FLOAT);
                break;
            case DECIMAL:
                p.printDecimal(decimalValue());
                break;
            default:
                // Shouldn't happen.
        }
    }

    public int compareTo(final NumericValue other) {
        Type precision = maxPrecision(this, other);
//System.out.println("comparing " + a + " with " + b + " (precision = " + precision + ", a.getType() = " + a.getType() + ", b.getType() = " + b.getType() + ")");

        switch (precision) {
            case INTEGER:
                return compare(this.intValue(), other.intValue());
            case LONG:
                return compare(this.longValue(), other.longValue());
            case FLOAT:
                return compare(this.floatValue(), other.floatValue());
            case DOUBLE:
                return compare(this.doubleValue(), other.doubleValue());
            case DECIMAL:
//System.out.println("    a.decimalValue().compareTo( b.decimalValue() ) = " + a.decimalValue().compareTo( b.decimalValue() ));
//System.out.println("    a.number.getClass() = " + a.number.getClass() + ", b.number.getClass() = " + b.number.getClass());
                return this.decimalValue().compareTo(other.decimalValue());
            default:
                // Shouldn't happen.
                return 0;
        }
    }

    public static int compareNumericLiterals(final Literal a, final Literal b) {
        Type aType = inferPrecision(a);
        Type bType = inferPrecision(b);

        if (null == aType || null == bType) {
            throw new IllegalArgumentException("literal has non-numeric type");
        }

        Type precision = maxPrecision(aType, bType);

        switch (precision) {
            case INTEGER:
                return compare(a.intValue(), b.intValue());
            case LONG:
                return compare(a.longValue(), b.longValue());
            case FLOAT:
                return compare(a.floatValue(), b.floatValue());
            case DOUBLE:
                return compare(doubleValue(a), doubleValue(b));
            case DECIMAL:
                return a.decimalValue().compareTo(b.decimalValue());
            default:
                // Shouldn't happen.
                return 0;
        }
    }

    public static int compare(final Literal a, final NumericValue b) {
        Type aType = inferPrecision(a);

        if (null == aType) {
            throw new IllegalArgumentException("literal has non-numeric type: " + a);
        } else {
            Type precision = maxPrecision(aType, b.getDatatype());

            switch (precision) {
                case INTEGER:
                    return compare(a.intValue(), b.intValue());
                case LONG:
                    return compare(a.longValue(), b.longValue());
                case FLOAT:
                    return compare(a.floatValue(), b.floatValue());
                case DOUBLE:
                    return compare(doubleValue(a), b.doubleValue());
                case DECIMAL:
                    return a.decimalValue().compareTo(b.decimalValue());
                default:
                    // Shouldn't happen.
                    return 0;
            }
        }
    }

    // Note: the literal is assumed to be of type xsd:double.
    // TODO: FLOAT also has special values
    protected static double doubleValue(final Literal l) {
        String label = l.getLabel();

        // Sesame's literals apparently don't handle these special
        // cases, so we need to check for them here.
        if (label.equals("NaN")) {
            return Double.NaN;
        } else if (label.equals("INF")) {
            return Double.POSITIVE_INFINITY;
        } else if (label.equals("-INF")) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return l.doubleValue();
        }
    }

    private static int compare(final int a, final int b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare(final long a, final long b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare(final float a, final float b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare(final double a, final double b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    public String toString() {
        return number.toString();
    }

    public static boolean isNumericLiteral(final Literal l) {
        return null != inferPrecision(l);
    }

    private static Type inferPrecision(final Literal l) {
        URI datatype = l.getDatatype();

        return (null == datatype)
                ? null
                : uriToTypeMap.get(datatype);
    }

    protected static Type maxPrecision(final NumericValue a, final NumericValue b) {
        return maxPrecision(a.getDatatype(), b.getDatatype());
    }

    protected static Type maxPrecision(final Type a, final Type b) {
        Type max = Type.INTEGER;

        if (a == Type.LONG || b == Type.LONG) {
            max = Type.LONG;
        }

        if (a == Type.FLOAT || b == Type.FLOAT) {
            max = Type.FLOAT;
        }

        if (a == Type.DOUBLE || b == Type.DOUBLE) {
            max = Type.DOUBLE;
        }

        if (a == Type.DECIMAL || b == Type.DECIMAL) {
            max = Type.DECIMAL;
        }

        return max;
    }

    public boolean equals(final Object other) {
        return (other instanceof NumericValue) && ((NumericValue) other).getType().equals(type)
                && ((NumericValue) other).number.equals(number);
    }

    public int hashCode() {
        // TODO: is it possible for two NumericValues with the same type and
        // equivalent values to have 'number's with different hash codes?
        return 1983561912 + (type.hashCode() * number.hashCode());
    }

    public abstract NumericValue abs();

    public abstract NumericValue neg();

    public abstract NumericValue add(final NumericValue b);

    public abstract NumericValue sub(final NumericValue b);

    public abstract NumericValue mul(final NumericValue b);

    public abstract NumericValue div(final NumericValue b);

    public abstract NumericValue mod(final NumericValue b);

    public abstract NumericValue pow(final NumericValue pow);

    /*
    public static void main(final String[] args)
    {
        for ( Type type : Type.values() )
        {
            System.out.println("" + type + ": " + type.hashCode());
        }
    }*/

    public RippleValue.Type getType() {
        return RippleValue.Type.NUMERIC_TYPED_LITERAL;
    }
}

