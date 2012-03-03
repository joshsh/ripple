/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RDFImporter;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;

import java.io.InputStream;
import java.util.Iterator;

public class RippleListTest extends RippleTestCase
{
    public void testListRDFEquivalence() throws Exception
    {
        assertReducesTo( "(1 2 3) rdf:rest.", "(2 3)" );
        assertReducesTo( "(1 2 3) rdf:rest. rdf:first.", "2" );

        /* TODO: in order to pass, these test cases will require a new feature.
        assertReducesTo( "rdf:nil rdf:type >>", "rdf:List" );
        assertReducesTo( "() rdf:type >>", "rdf:List" );
        assertReducesTo( "(1 2 3) rdf:type >>", "rdf:List" );
        */
    }

    public void testFromRDF() throws Exception
    {
        final ModelConnection mc = getTestModel().createConnection();

        InputStream is = RippleListTest.class.getResourceAsStream( "listTest.ttl" );
        RDFImporter importer = new RDFImporter( mc );
        SesameInputAdapter.parse( is, importer, "", RDFFormat.TURTLE );
        mc.commit();
        is.close();

        RDFValue head;
        Collector<RippleList> created = new Collector<RippleList>();
        final Collector<RippleList> allowed = new Collector<RippleList>();

        Sink<RippleList> verifySink = new Sink<RippleList>()
        {
            public void put( final RippleList list ) throws RippleException
            {
                boolean found = false;

                for ( Iterator<RippleList> iter = allowed.iterator(); iter.hasNext(); )
                {
                    if ( 0 == mc.getComparator().compare( iter.next(), list ) )
                    {
                        found = true;
                        break;
                    }
                }

                assertTrue( found );
            }
        };

        RippleValue l1 = mc.typedValue("1", XMLSchema.STRING);
        RippleValue l2 = mc.typedValue("2", XMLSchema.STRING);
        RippleValue l1a = mc.typedValue("1a", XMLSchema.STRING);
        RippleValue l1b = mc.typedValue("1b", XMLSchema.STRING);
        RippleValue l2a = mc.typedValue("2a", XMLSchema.STRING);
        RippleValue l2b = mc.typedValue("2b", XMLSchema.STRING);

        head = new RDFValue( createURI( "urn:test.RippleListTest.FromRdfTest#simpleList", mc ) );
        created.clear();
        mc.toList( head, created );
        assertEquals( 1, created.size() );
        allowed.clear();
        allowed.put( mc.list().push(l2).push( l1 ) );
        created.writeTo( verifySink );

        head = new RDFValue( createURI( "urn:test.RippleListTest.FromRdfTest#firstBranchingList", mc ) );
        created.clear();
        mc.toList( head, created );
        assertEquals( 2, created.size() );
        allowed.clear();
        allowed.put( mc.list().push(l2).push( l1a ) );
        allowed.put(mc.list().push(l2).push(l1b));
        created.writeTo( verifySink );

        head = new RDFValue( createURI( "urn:test.RippleListTest.FromRdfTest#restBranchingList", mc ) );
        created.clear();
        mc.toList( head, created );
        assertEquals( 2, created.size() );
        allowed.clear();
        allowed.put( mc.list().push(l2a).push( l1 ) );
        allowed.put( mc.list().push(l2b).push( l1 ) );
        created.writeTo( verifySink );

        head = new RDFValue( createURI( "urn:test.RippleListTest.FromRdfTest#firstAndRestBranchingList", mc ) );
        created.clear();
        mc.toList( head, created );
        assertEquals( 4, created.size() );
        allowed.clear();
        allowed.put( mc.list().push(l2a).push( l1a ) );
        allowed.put( mc.list().push(l2a).push( l1b ) );
        allowed.put( mc.list().push(l2b).push( l1a ) );
        allowed.put( mc.list().push(l2b).push( l1b ) );
        created.writeTo( verifySink );

        // Note: the circular list is not tested.

        mc.close();
    }

    public void testListConcatenation() throws Exception
    {
        ModelConnection mc = getTestModel().createConnection();
        RippleValue
            a0 = mc.numericValue(42),
            a1 = mc.numericValue(137),
            a2 = mc.numericValue(23),
            b0 = mc.numericValue(216);
        mc.close();

        RippleList
            listA = mc.list().push(a2).push( a1 ).push( a0 ),
            listB = mc.list().push(b0);

        assertEquals( listA.length(), 3 );
        assertEquals( listA.get( 0 ), a0 );
        assertEquals( listA.get( 1 ), a1 );
        assertEquals( listA.get( 2 ), a2 );
        assertEquals( listB.length(), 1 );
        assertEquals( listB.get( 0 ), b0 );
        assertEquals( mc.list().length(), 0 );

        RippleList lresult;

        lresult = listA.concat( listB );
        assertEquals( lresult.length(), 4 );
        assertEquals( lresult.get( 0 ), a0 );
        assertEquals( lresult.get( 1 ), a1 );
        assertEquals( lresult.get( 2 ), a2 );
        assertEquals( lresult.get( 3 ), b0 );
        lresult = listB.concat( listA );
        assertEquals( lresult.length(), 4 );
        assertEquals( lresult.get( 0 ), b0 );
        assertEquals( lresult.get( 1 ), a0 );
        assertEquals( lresult.get( 2 ), a1 );
        assertEquals( lresult.get( 3 ), a2 );

        lresult = listA.concat( mc.list() );
        assertEquals( lresult.length(), 3 );
        assertEquals( lresult.get( 0 ), a0 );
        assertEquals( lresult.get( 1 ), a1 );
        assertEquals( lresult.get( 2 ), a2 );

        lresult = mc.list().concat( listB );
        assertEquals( lresult.length(), 1 );
        assertEquals( lresult.get( 0 ), b0 );

        lresult = mc.list().concat( mc.list() );
        assertEquals( lresult.length(), 0 );
        assertEquals( lresult, mc.list() );
    }
}

