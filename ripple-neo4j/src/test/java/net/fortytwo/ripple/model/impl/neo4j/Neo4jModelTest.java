/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import junit.framework.TestCase;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:53:03 PM
 */
public class Neo4jModelTest extends TestCase {
    private boolean initialized = false;
 
    public void setUp() throws RippleException {
        if (!initialized) {
            Ripple.initialize();
            initialized = true;
        }
    }

    public void testNeo4j() throws Exception {
        NeoService neo = new EmbeddedNeo( "var/neo" );

        Transaction tx = Transaction.begin();
        
        try
        {
            Node firstNode = neo.createNode();
            Node secondNode = neo.createNode();
            Relationship relationship = firstNode.createRelationshipTo( secondNode, MyRelationshipTypes.KNOWS );

            firstNode.setProperty( "message", "Hello, " );
            secondNode.setProperty( "message", "world!" );
            relationship.setProperty( "message", "brave Neo " );

            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );

            tx.success();
        }

        finally
        {
            tx.finish();
        }

        neo.shutdown();
    }

/*    public void testModel() throws Exception {
        UriMap uriMap = new UriMap();
        Model model = new Neo4jModel(uriMap);
        StackEvaluator eval = new LazyStackEvaluator();
        QueryEngine qe = new QueryEngine(model, eval, System.out, System.err);

        
    }  */

    private enum MyRelationshipTypes implements RelationshipType
    {
        KNOWS
    }
}
