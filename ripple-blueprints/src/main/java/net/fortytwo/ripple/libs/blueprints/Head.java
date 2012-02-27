package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 8:11 PM
 */
public class Head extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2011_08 + "head"
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("edge", "an edge, which has exactly one head", true)};

    }

    public String getComment() {
        return "finds the head of an edge (the vertex from which the edge is incoming)." +
                " The inverse mapping finds the incoming edges of a vertex.";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        RippleValue first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof EdgeValue) {
            Edge edge = ((EdgeValue) first).getElement();
            solutions.put(stack.push(new VertexValue(edge.getInVertex())));
        }
    }

    public StackMapping getInverse() {
        return inverseMapping;
    }

    private final StackMapping thisMapping = this;

    private final StackMapping inverseMapping = new StackMapping() {
        public int arity() {
            return 1;
        }

        public StackMapping getInverse() throws RippleException {
            return thisMapping;
        }

        public boolean isTransparent() {
            return true;
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
            RippleList stack = arg;
            RippleValue first = stack.getFirst();
            stack = stack.getRest();

            if (first instanceof VertexValue) {
                Vertex vertex = ((VertexValue) first).getElement();

                for (Edge edge : vertex.getInEdges()) {
                    solutions.put(stack.push(new EdgeValue(edge)));
                }
            }
        }
    };
}
