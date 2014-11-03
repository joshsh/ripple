package net.fortytwo.flow.rdf.ranking;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.sail.SailConnection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFSpreadVector implements WeightedVectorApproximation<Resource, HandlerException> {
    //    private static final double DECAY = 0.1;
    private static final double DECAY = 0.85;

    private final WeightedVector<Resource> vector;
    private Queue<Resource> curGen;
    private Queue<Resource> nextGen;
    private double weight;
    private final SailConnection sailConnection;
    private final Resource[] seeds;
    private final URI[] inPredicates, outPredicates;

    public RDFSpreadVector(final WeightedVector<Resource> vector,
                           final URI[] inPredicates,
                           final URI[] outPredicates,
                           final SailConnection sailConnection,
                           final Resource... seeds) {
        this.vector = vector;
        this.inPredicates = inPredicates;
        this.outPredicates = outPredicates;
        this.sailConnection = sailConnection;
        this.seeds = seeds;

        curGen = new LinkedList<Resource>();
        curGen.addAll(Arrays.asList(seeds));

        nextGen = new LinkedList<Resource>();

        weight = 1.0;
    }

    public WeightedVector<Resource> currentResult() {
        // Note: don't normalize the intermediate vector, in case we continue extending it.
        WeightedVector<Resource> normed = new WeightedVector<Resource>(vector);

        for (Resource s : seeds) {
            normed.setWeight(s, 0.0);
        }

        normed.normalizeAsDist();

        return normed;
    }

    public int compute(int cycles) throws HandlerException {
        for (int i = 0; i < cycles; i++) {
            if (0 == curGen.size()) {
                if (0 == nextGen.size()) {
                    return 0;
                } else {
                    weight *= DECAY;
                    Queue<Resource> tmp = curGen;
                    curGen = nextGen;
                    nextGen = tmp;
                }
            } else {
                Resource r = curGen.remove();

                stepRelated(sailConnection, r, weight, vector, nextGen);
            }
        }

        return cycles;
    }

    public void stepRelated(final SailConnection sc,
                            final Resource resource,
                            final double weight,
                            final WeightedVector<Resource> result,
                            final Queue<Resource> resources) throws HandlerException {
        if (0 < inPredicates.length) {
            Handler<Resource, HandlerException> inHandler = new Handler<Resource, HandlerException>() {
                public boolean handle(final Resource r) throws HandlerException {
                    result.addWeight(r, weight);
                    resources.offer(r);
                    return true;
                }
            };

            Ranking.traverseBackward(sc,
                    new KeepResourcesFilter(inHandler),
                    resource,
                    inPredicates);
        }

        if (0 < outPredicates.length) {
            Handler<Resource, HandlerException> outHandler = new Handler<Resource, HandlerException>() {
                public boolean handle(final Resource r) throws HandlerException {
                    result.addWeight(r, weight);
                    resources.offer(r);
                    return true;
                }
            };

            Ranking.traverseForward(sc,
                    new KeepResourcesFilter(outHandler),
                    resource,
                    outPredicates);
        }
    }
}
