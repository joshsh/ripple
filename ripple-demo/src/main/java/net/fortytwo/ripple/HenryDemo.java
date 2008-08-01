package net.fortytwo.ripple;

import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.query.StackEvaluator;
import net.fortytwo.ripple.query.LazyEvaluator;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.QueryPipe;
import net.fortytwo.ripple.cli.TurtleView;

/**
 * Author: josh
 * Date: Apr 25, 2008
 * Time: 9:44:52 PM
 */
public class HenryDemo {
    public static void main(final String[] args) throws Exception {
        Ripple.initialize();

        // Create a base Sail for storing data (this could just as well be a
        // persistent Sail such as NativeStore, in which case you wouldn't
        // need a cache file.
        Sail baseSail = new MemoryStore();
        baseSail.initialize();

        // Instantiate LinkedDataSail.
        URIMap uriMap = new URIMap();
		Sail sail = new LinkedDataSail( baseSail, uriMap );
		sail.initialize();

		// Attach a Ripple model to the repository.
		Model model = new SesameModel( sail, Ripple.class.getResource("libraries.txt"), uriMap );

		// Attach a query engine to the model.
		StackEvaluator evaluator = new LazyEvaluator();
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
        sail.shutDown();
        baseSail.shutDown();
        System.exit( 0 );
    }
}
