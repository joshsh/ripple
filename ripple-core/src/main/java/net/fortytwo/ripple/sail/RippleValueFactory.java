package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.BNode;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleLiteral;
import org.openrdf.sail.SailException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleValueFactory implements ValueFactory {
    private static final String STRING_NAMESPACE = "urn:string:";

    private static final Random random = new Random();

    private final ValueFactory base;

    public RippleValueFactory(final ValueFactory base) {
        this.base = base;
    }

    public Value nativize(final Value other) throws SailException {
        if (other instanceof IRI) {
            String s = other.stringValue();
            if (s.startsWith(STRING_NAMESPACE)) {
                try {
                    return createLiteral(StringUtils.percentDecode(s.substring(STRING_NAMESPACE.length())));
                } catch (RippleException e) {
                    throw new SailException(e);
                }
            } else {
                return createIRI(other.stringValue());
            }
        } else if (other instanceof Literal) {
            Literal l = (Literal) other;
            if (null != l.getDatatype()) {
                return createLiteral(l.getLabel(), l.getDatatype());
            } else if (l.getLanguage().isPresent()) {
                return createLiteral(l.getLabel(), l.getLanguage().get());
            } else {
                return createLiteral(l.getLabel());
            }
        } else if (other instanceof BNode) {
            return createBNode(((BNode) other).getID());
        } else {
            throw new IllegalStateException("value of unexpected class: " + other);
        }
    }

    @Override
    public IRI createIRI(String s) {
        return new RippleURI(s);
    }

    @Override
    public IRI createIRI(String s, String s1) {
        return new RippleURI(s + s1);
    }

    @Override
    public BNode createBNode() {
        return new RippleBNode();
    }

    @Override
    public BNode createBNode(String s) {
        return new RippleBNode(s);
    }

    @Override
    public Literal createLiteral(String s) {
        return new RippleLiteral(s);
    }

    @Override
    public Literal createLiteral(String s, String s1) {
        return new RippleLiteral(s, s1);
    }

    @Override
    public Literal createLiteral(String s, IRI uri) {
        return new RippleLiteral(s, uri);
    }

    @Override
    public Literal createLiteral(boolean b) {
        Literal other = base.createLiteral(b);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(byte b) {
        Literal other = base.createLiteral(b);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(short i) {
        Literal other = base.createLiteral(i);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(int i) {
        Literal other = base.createLiteral(i);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(long l) {
        Literal other = base.createLiteral(l);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(float v) {
        Literal other = base.createLiteral(v);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(double v) {
        Literal other = base.createLiteral(v);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(BigDecimal bigDecimal) {
        throw new UnsupportedOperationException("big numbers are not yet supported");
    }

    @Override
    public Literal createLiteral(BigInteger bigInteger) {
        throw new UnsupportedOperationException("big numbers are not yet supported");
    }

    @Override
    public Literal createLiteral(XMLGregorianCalendar xmlGregorianCalendar) {
        Literal other = base.createLiteral(xmlGregorianCalendar);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Literal createLiteral(Date date) {
        Literal other = base.createLiteral(date);
        return new RippleLiteral(other.getLabel(), other.getDatatype());
    }

    @Override
    public Statement createStatement(Resource resource, IRI uri, Value value) {
        // Note: it is assumed that the argument values were also produced by this ValueFactory.
        return base.createStatement(resource, uri, value);
    }

    @Override
    public Statement createStatement(Resource resource, IRI uri, Value value, Resource resource1) {
        return base.createStatement(resource, uri, value, resource1);
    }

    /**
     * @author Joshua Shinavier (http://fortytwo.net)
     */
    public class RippleLiteral extends SimpleLiteral implements RippleSesameValue, BNode {
        private RippleList list = null;

        public RippleLiteral(String label) {
            super(label);
        }

        public RippleLiteral(String label, String language) {
            super(label, language);
        }

        public RippleLiteral(String label, IRI datatype) {
            super(label, datatype);
        }

        @Override
        public RippleList getStack() {
            return list;
        }

        @Override
        public void setStack(final RippleList list) {
            this.list = list;
        }

        @Override
        public Value getNativeValue() {
            return this.getLanguage().isPresent()
                    ? base.createLiteral(this.getLabel(), this.getLanguage().get())
                    : null != this.getDatatype()
                    ? base.createLiteral(this.getLabel(), this.getDatatype())
                    : base.createLiteral(this.getLabel());
        }

        @Override
        public String getID() {
            return "rl" + random.nextInt(Integer.MAX_VALUE);
        }
    }
}
