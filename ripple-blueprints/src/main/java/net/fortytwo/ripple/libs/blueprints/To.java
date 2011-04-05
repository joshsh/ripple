package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.StackMapping;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 8:11 PM
 */
public class To extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2011_04 + "to"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("vertex", "the vertex from which to find incoming edges", true)};
    }

    @Override
    public String getComment() {
        return "finds the incoming edges incident on a given Blueprints vertex";
    }

    @Override
    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions) throws RippleException {
        RippleList stack = arg.getStack();
        RippleValue first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof VertexValue) {
            Vertex vertex = ((VertexValue) first).getVertex();

            for (Edge edge : vertex.getInEdges()) {
                solutions.put(arg.with(stack.push(new EdgeValue(edge))));
            }
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
        public void apply(final StackContext arg,
                          final Sink<StackContext, RippleException> solutions) throws RippleException {

            RippleList stack = arg.getStack();
            RippleValue first = stack.getFirst();
            stack = stack.getRest();

            if (first instanceof EdgeValue) {
                Edge edge = ((EdgeValue) first).getEdge();
                solutions.put(arg.with(stack.push(new VertexValue(edge.getInVertex()))));
            }
        }
    };
}
