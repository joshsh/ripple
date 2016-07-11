package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Tail extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2013_03 + "tail"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("edge", "an edge, which has exactly one tail", true)};
    }

    @Override
    public String getComment() {
        return "finds the tail of an edge (the vertex from which the edge is outgoing)." +
                " The inverse mapping finds the outgoing edges of a vertex.";
    }

    @Override
    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        Object first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof Edge) {
            Edge edge = (Edge) first;
            solutions.accept(stack.push(edge.getVertex(Direction.OUT)));
        }
    }

    @Override
    public StackMapping getInverse() {
        return inverseMapping;
    }

    private final StackMapping thisMapping = this;

    private final StackMapping inverseMapping = new StackMapping() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public StackMapping getInverse() throws RippleException {
            return thisMapping;
        }

        @Override
        public boolean isTransparent() {
            return true;
        }

        @Override
        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            RippleList stack = arg;
            Object first = stack.getFirst();
            stack = stack.getRest();

            if (first instanceof Vertex) {
                Vertex vertex = (Vertex) first;

                for (Edge edge : vertex.getEdges(Direction.OUT)) {
                    solutions.accept(stack.push(edge));
                }
            }
        }
    };
}
