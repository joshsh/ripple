package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class NumericType<T> implements RippleType<T> {

    public enum Datatype {
        DECIMAL, DOUBLE, FLOAT, INTEGER, LONG
    }

    protected static final Map<Class, Datatype> datatypeByClass;
    protected static final Map<URI, Datatype> datatypeByUri;

    static {
        datatypeByClass = new HashMap<>();
        datatypeByClass.put(BigDecimal.class, Datatype.DECIMAL);
        datatypeByClass.put(Double.class, Datatype.DOUBLE);
        datatypeByClass.put(Float.class, Datatype.FLOAT);
        datatypeByClass.put(Integer.class, Datatype.INTEGER);
        datatypeByClass.put(Long.class, Datatype.LONG);

        datatypeByUri = new HashMap<>();
        datatypeByUri.put(XMLSchema.DECIMAL, Datatype.DECIMAL);
        datatypeByUri.put(XMLSchema.DOUBLE, Datatype.DOUBLE);
        datatypeByUri.put(XMLSchema.FLOAT, Datatype.FLOAT);
        datatypeByUri.put(XMLSchema.INTEGER, Datatype.INTEGER);
        datatypeByUri.put(XMLSchema.LONG, Datatype.LONG);
    }

    public abstract Datatype findDatatype(T instance);

    public abstract Number findNumber(T instance, Datatype datatype);

    public Number findNumber(T instance) {
        return findNumber(instance, findDatatype(instance));
    }

    @Override
    public StackMapping getMapping(T instance) {
        return null;
    }

    @Override
    public RippleType.Category getCategory() {
        return RippleType.Category.NUMERIC_TYPED_LITERAL;
    }

    protected void printTo(final Number instance,
                           final Datatype datatype,
                           final RipplePrintStream p) throws RippleException {
        switch (datatype) {
            case INTEGER:
                p.printInteger(instance.intValue());
                break;
            case LONG:
                p.printTypedLiteral(toString(), XMLSchema.LONG);
                break;
            case DOUBLE:
                p.printDouble(instance.doubleValue());
                break;
            case FLOAT:
                p.printTypedLiteral(toString(), XMLSchema.FLOAT);
                break;
            case DECIMAL:
                p.printDecimal(decimalValue(instance));
                break;
            default:
                throw new IllegalStateException();
        }
    }

    protected Value toRDF(final Number instance, final Datatype datatype, final ModelConnection mc) throws RippleException {
        SesameModelConnection smc = (SesameModelConnection) mc;
        Value rdfEquivalent;

        switch (datatype) {
            case INTEGER:
                // Don't use ValueFactory.creatLiteral(int), which (at
                // least in this case) produces xsd:int instead of xsd:integer
                rdfEquivalent =
                        smc.getValueFactory().createLiteral("" + instance.intValue(), XMLSchema.INTEGER);
                break;
            case LONG:
                rdfEquivalent =
                        smc.getValueFactory().createLiteral(instance.longValue());
                break;
            case DOUBLE:
                rdfEquivalent =
                        smc.getValueFactory().createLiteral(instance.doubleValue());
                break;
            case FLOAT:
                rdfEquivalent =
                        smc.getValueFactory().createLiteral(instance.floatValue());
                break;
            case DECIMAL:
                rdfEquivalent =
                        smc.getValueFactory().createLiteral(instance.toString(), XMLSchema.DECIMAL);
                break;
            default:
                throw new IllegalStateException();
        }

        return rdfEquivalent;
    }

    private static BigDecimal decimalValue(final Number instance) {
        return (instance instanceof BigDecimal)
                ? (BigDecimal) instance
                : BigDecimal.valueOf(instance.doubleValue());
    }

    public static Datatype datatypeOf(final Number n) {
        Datatype p = datatypeByClass.get(n.getClass());
        if (null == p) {
            throw new IllegalArgumentException("unsupported Number subclass: " + n.getClass());
        }

        return p;
    }

    public static Datatype datatypeOf(final Number a, final Number b) {
        Datatype da = datatypeOf(a);
        Datatype db = datatypeOf(b);

        Datatype max = Datatype.INTEGER;

        if (da == Datatype.LONG || db == Datatype.LONG) {
            max = Datatype.LONG;
        }

        if (da == Datatype.FLOAT || db == Datatype.FLOAT) {
            max = Datatype.FLOAT;
        }

        if (da == Datatype.DOUBLE || db == Datatype.DOUBLE) {
            max = Datatype.DOUBLE;
        }

        if (da == Datatype.DECIMAL || db == Datatype.DECIMAL) {
            max = Datatype.DECIMAL;
        }

        return max;
    }

    protected static Datatype dataTypeOf(final Literal l) {
        URI datatype = l.getDatatype();

        return (null == datatype)
                ? null
                : datatypeByUri.get(datatype);
    }

    public static boolean isNumericLiteral(final Literal l) {
        return null != dataTypeOf(l);
    }

    public static int compare(final Number a, final Number b) {
        Datatype d = datatypeOf(a, b);

        switch (d) {
            case INTEGER:
                return ((Integer) a.intValue()).compareTo(b.intValue());
            case LONG:
                return ((Long) a.longValue()).compareTo(b.longValue());
            case FLOAT:
                return ((Float) a.floatValue()).compareTo(b.floatValue());
            case DOUBLE:
                return ((Double) a.doubleValue()).compareTo(b.doubleValue());
            case DECIMAL:
                return decimalValue(a).compareTo(decimalValue(b));
            default:
                throw new IllegalStateException();
        }
    }

    public static int sign(final Number n) {
        double x = n.doubleValue();
        return x < 0.0 ? -1 : x > 0 ? 1 : 0;
    }

    public static boolean isZero(final Number n) {
        return (0.0 == n.doubleValue());
    }

    public static Number abs(final Number n) {
        switch (datatypeOf(n)) {
            case INTEGER:
                return Math.abs(n.intValue());
            case LONG:
                return Math.abs(n.longValue());
            case FLOAT:
                return Math.abs(n.floatValue());
            case DOUBLE:
                return Math.abs(n.doubleValue());
            case DECIMAL:
                return decimalValue(n).abs();
            default:
                throw new IllegalStateException();
        }
    }

    public static Number neg(final Number n) {
        switch (datatypeOf(n)) {
            case INTEGER:
                return -n.intValue();
            case LONG:
                return -n.longValue();
            case FLOAT:
                return -n.floatValue();
            case DOUBLE:
                // Note: avoids negative zero.
                return 0.0 - n.doubleValue();
            case DECIMAL:
                return decimalValue(n).negate();
            default:
                throw new IllegalStateException();
        }
    }

    public static Number add(final Number a, final Number b) {
        switch (datatypeOf(a, b)) {
            case INTEGER:
                return a.intValue() + b.intValue();
            case LONG:
                return a.longValue() + b.longValue();
            case FLOAT:
                return a.floatValue() + b.floatValue();
            case DOUBLE:
                return a.doubleValue() + b.doubleValue();
            case DECIMAL:
                return decimalValue(a).add(decimalValue(b));
            default:
                throw new IllegalStateException();
        }
    }

    public static Number sub(final Number a, final Number b) {
        switch (datatypeOf(a, b)) {
            case INTEGER:
                return a.intValue() - b.intValue();
            case LONG:
                return a.longValue() - b.longValue();
            case FLOAT:
                return a.floatValue() - b.floatValue();
            case DOUBLE:
                return a.doubleValue() - b.doubleValue();
            case DECIMAL:
                return decimalValue(a).subtract(decimalValue(b));
            default:
                throw new IllegalStateException();
        }
    }

    public static Number mul(final Number a, final Number b) {
        switch (datatypeOf(a, b)) {
            case INTEGER:
                return a.intValue() * b.intValue();
            case LONG:
                return a.longValue() * b.longValue();
            case FLOAT:
                return a.floatValue() * b.floatValue();
            case DOUBLE:
                return a.doubleValue() * b.doubleValue();
            case DECIMAL:
                return decimalValue(a).multiply(decimalValue(b));
            default:
                throw new IllegalStateException();
        }
    }

    public static Number div(final Number a, final Number b) {
        switch (datatypeOf(a, b)) {
            case INTEGER:
                return a.intValue() / b.intValue();
            case LONG:
                return a.longValue() / b.longValue();
            case FLOAT:
                return a.floatValue() / b.floatValue();
            case DOUBLE:
                return a.doubleValue() / b.doubleValue();
            case DECIMAL:
                return decimalValue(a).divide(decimalValue(b));
            default:
                throw new IllegalStateException();
        }
    }

    public static Number mod(final Number a, final Number b) {
        switch (datatypeOf(a, b)) {
            case INTEGER:
                return a.intValue() % b.intValue();
            case LONG:
                return a.longValue() % b.longValue();
            case FLOAT:
                return a.floatValue() % b.floatValue();
            case DOUBLE:
                return a.doubleValue() % b.doubleValue();
            case DECIMAL:
                return decimalValue(a).remainder(decimalValue(b)).abs();
            default:
                throw new IllegalStateException();
        }
    }

    public static Number pow(final Number base, final Number exp) {
        Datatype baseP = datatypeOf(base);
        Datatype expP = datatypeOf(exp);
        if (Datatype.DECIMAL == baseP && Datatype.INTEGER == expP) {
            return decimalValue(base).pow(exp.intValue());
        } else {
            double r = Math.pow(base.doubleValue(), exp.doubleValue());
            Datatype datatype = datatypeOf(base, exp);
            switch (datatype) {
                case INTEGER:
                    return (int) r;
                case LONG:
                    return (long) r;
                case FLOAT:
                    return (float) r;
                case DOUBLE:
                    return r;
                case DECIMAL:
                    return r;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    // Note: the literal is assumed to be of type xsd:double.
    // TODO: FLOAT also has special values
    protected static double doubleValue(final Literal l) {
        String label = l.getLabel();

        // Sesame's literals apparently don't handle these special
        // cases, so we need to check for them here.
        switch (label) {
            case "NaN":
                return Double.NaN;
            case "INF":
                return Double.POSITIVE_INFINITY;
            case "-INF":
                return Double.NEGATIVE_INFINITY;
            default:
                return l.doubleValue();
        }
    }

    @Override
    public int compare(T o1, T o2, ModelConnection mc) {
        return ((Double) findNumber(o1).doubleValue()).compareTo(findNumber(o2).doubleValue());
    }
}
