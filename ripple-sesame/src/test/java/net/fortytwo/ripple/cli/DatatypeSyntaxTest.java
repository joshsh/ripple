package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;

import java.util.Collection;

import org.openrdf.model.Literal;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class DatatypeSyntaxTest extends RippleTestCase
{
    public void testBoolean() throws Exception
    {
        Collection<RippleList> results;
        RippleList stack;
        Literal l;
        ModelConnection mc = getTestModel().getConnection(null);

        results = reduce( "true" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "true", l.getLabel() );
        assertEquals( XMLSchema.BOOLEAN, l.getDatatype() );

        results = reduce( "false" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "false", l.getLabel() );
        assertEquals( XMLSchema.BOOLEAN, l.getDatatype() );

        mc.close();
    }

    public void testInteger() throws Exception
    {
        Collection<RippleList> results;
        RippleList stack;
        Literal l;
        ModelConnection mc = getTestModel().getConnection(null);

        results = reduce( "0" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        results = reduce( "10" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "10", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        // Leading "-" sign
        results = reduce( "-5" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "-5", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        // Leading "+" sign
        results = reduce( "+1" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "1", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        // Leading zeroes
        results = reduce( "042" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "42", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        mc.close();
    }

    public void testDouble() throws Exception
    {
        Collection<RippleList> results;
        RippleList stack;
        Literal l;
        ModelConnection mc = getTestModel().getConnection(null);

        results = reduce( "0.0e0" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0.0", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Leading '-' on exponent.
        results = reduce( "10E-02" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0.1", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Leading '+' on exponent.
        results = reduce( "10.1E+2" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "1010.0", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Extra trailing zeroes are preserved (although according to the XML
        // Schema specification, they don't need to be).
        results = reduce( "0.00e0" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0.0", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Leading "-" on mantissa
        results = reduce( "-5e23" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "-5.0E23", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Leading "+" on mantissa
        results = reduce( "+1.2e1" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "12.0", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        // Leading zeroes
        results = reduce( "00042E99" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "4.2E100", l.getLabel() );
        assertEquals( XMLSchema.DOUBLE, l.getDatatype() );

        mc.close();
    }

    public void testDecimal() throws Exception
    {
        Collection<RippleList> results;
        RippleList stack;
        Literal l;
        ModelConnection mc = getTestModel().getConnection(null);

        results = reduce( "0.0" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0.0", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        results = reduce( "10.1" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "10.1", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        // Extra trailing zeroes are preserved (although according to the XML
        // Schema specification, they don't need to be).
        results = reduce( "0.00" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0.00", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        // Leading "-" sign
        results = reduce( "-5.1" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "-5.1", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        // Leading "+" sign
        results = reduce( "+1.2" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "1.2", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        // Leading zeroes
        results = reduce( "00042.09" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "42.09", l.getLabel() );
        assertEquals( XMLSchema.DECIMAL, l.getDatatype() );

        mc.close();
    }
}