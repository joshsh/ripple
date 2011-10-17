/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.io.RDFImporter;
import net.fortytwo.ripple.libs.stack.Dup;
import net.fortytwo.ripple.model.ListDequotation;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RDFPredicateMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.RDFUtils;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.RDFFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OperatorTest extends RippleTestCase
{
	private static final String TEST_1 =
  "@prefix : <urn:test.CreateOperatorTest#>.\n"
+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n"
+ "\n"
+ ":simpleList a rdf:List;\n"
+ "    rdf:first \"1\";\n"
+ "    rdf:rest [\n"
+ "        rdf:first \"2\";\n"
+ "        rdf:rest rdf:nil ];\n"
+ "    .\n"
+ ":firstBranchingList a rdf:List;\n"
+ "rdf:first \"1a\";\n"
+ "rdf:first \"1b\";\n"
+ "rdf:rest [\n"
+ "		rdf:first \"2\";\n"
+ "		rdf:rest rdf:nil ];\n"
+ ".";
	
    public void testCreateOperator() throws Exception
    {
        Model model = getTestModel();
        ModelConnection mc = model.createConnection();

        InputStream is = new ByteArrayInputStream( TEST_1.getBytes() );
        RDFImporter importer = new RDFImporter( mc );
        SesameInputAdapter sc = new SesameInputAdapter( importer );
        RDFUtils.read( is, sc, "", RDFFormat.TURTLE );
        mc.commit();
        is.close();

        Collector<Operator> ops = new Collector<Operator>();
        RippleValue arg;
        RippleValue a1 = mc.plainValue("1");
        RippleValue a2 = mc.plainValue("2");

        // a RippleList --> ListDequotation
        arg = mc.list().push(a2).push( a1 );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof ListDequotation);

        // the nil RippleList --> ListDequotation
        arg = mc.list();
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof ListDequotation );

        // rdf:nil --> ListDequotation
        arg = new RDFValue( RDF.NIL );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof ListDequotation );

        // an rdf:List --> ListDequotation
        arg = new RDFValue( createURI( "urn:test.CreateOperatorTest#simpleList", mc ) );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof ListDequotation );

        // a branching rdf:List --> multiple ListDequotations
        arg = new RDFValue( createURI( "urn:test.CreateOperatorTest#firstBranchingList", mc ) );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 2, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof ListDequotation );

        // a PrimitiveStackRelation --> the same PrimitiveStackRelation
        arg = new Dup();
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof Dup );

        // an rdf:Property --> RdfPredicateRelation
        arg = new RDFValue( RDF.TYPE );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof RDFPredicateMapping);

        // a non-property RdfValue which is not anything else --> RdfPredicateRelation
        arg = new RDFValue( RDF.BAG );
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof RDFPredicateMapping);

        // anything else --> NullFilter
        arg = mc.numericValue(42);
        ops.clear();
        Operator.createOperator( arg, ops, mc );
        assertEquals( 1, ops.size() );
        assertTrue( ops.iterator().next().getMapping() instanceof NullStackMapping);

        mc.close();
    }
}
