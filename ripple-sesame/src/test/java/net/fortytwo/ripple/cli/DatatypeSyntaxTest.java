package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.NewRippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Collector;

import java.util.Collection;

import org.openrdf.model.Literal;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class DatatypeSyntaxTest extends NewRippleTestCase
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

        results = reduce( "10" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "10", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        results = reduce( "-5" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "-5", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        results = reduce( "+1" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "1", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        results = reduce( "0" );
        assertEquals( 1, results.size() );
        stack = results.iterator().next();
        assertEquals( 1, stack.length() );
        l = (Literal) stack.getFirst().toRDF(mc).sesameValue();
        assertEquals( "0", l.getLabel() );
        assertEquals( XMLSchema.INTEGER, l.getDatatype() );

        mc.close();
    }

    public void testDouble() throws Exception
    {

    }

    public void testDecimal() throws Exception
    {

    }
}