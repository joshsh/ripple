package net.fortytwo.ripple;

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.EmbeddedNeo;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.neo4j.Neo4jModel;
import net.fortytwo.ripple.query.StackEvaluator;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.QueryPipe;
import net.fortytwo.ripple.cli.TurtleView;

/**
 * Author: josh
 * Date: Apr 26, 2008
 * Time: 1:28:12 PM
 */
public class NeoRippleDemo {
    public static void main(final String[] args) throws Exception {
        Ripple.initialize();

        URIMap uriMap = new URIMap();

		// Attach a Ripple model to the repository.
        NeoService service = new EmbeddedNeo( "var/neo" );
        Model model = new Neo4jModel( service, Ripple.class.getResource( "libraries.txt" ), uriMap );

		// Attach a query engine to the model.
		StackEvaluator evaluator = new LazyStackEvaluator();
		QueryEngine queryEngine
			    = new QueryEngine( model, evaluator, System.out, System.err );

        // Create a connection to thew Ripple model.
        ModelConnection mc = queryEngine.getConnection();

        // This is a "sink" which counts and displays all of the resources which
        // are piped into it.  We'll use it to display query results.
        TurtleView view = new TurtleView( queryEngine.getPrintStream(), mc );

        // Add a parser pipeline for evaluating plain-text queries.
        // Feed results into the "Turtle view"
        QueryPipe queryPipe = new QueryPipe( queryEngine, view );

        // Evaluate a query.
        queryPipe.put( "@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n"
                + "40 2 add >> ."
                + ":timbl >> foaf:name >> ." );

        // All done.
        mc.close();
        service.shutdown();
        System.exit( 0 );
    }
}
