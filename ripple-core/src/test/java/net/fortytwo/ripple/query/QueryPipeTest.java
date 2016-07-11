package net.fortytwo.ripple.query;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Random;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class QueryPipeTest extends RippleTestCase {
    private static final int
            REPEAT = 10,
            MIN_EXPR_LENGTH = 0,
            MAX_EXPR_LENGTH = 50;
    private final Random rand = new Random();

    @Test
    public void testQueries() throws Exception {
        Model model = getTestModel();
        StackEvaluator eval = new LazyStackEvaluator();
        QueryEngine qe = new QueryEngine(model, eval, System.out, System.err);
        Collector<RippleList> expected = new Collector<>();
        Collector<RippleList> results = new Collector<>();
        QueryPipe qp = new QueryPipe(qe, results);
        ModelConnection mc = qe.getConnection();

        Number
                zero = 0,
                four = 4,
                five = 5;

        // A simple expression.
        results.clear();
        qp.accept("2 3 add .\n");
        expected.clear();
        expected.accept(createStack(mc, five));
        assertCollectorsEqual(expected, results);

        // A slightly more complex expression.
        results.clear();
        qp.accept("105"
                + " ((1 2 3 4 5) 0 add fold.) {7}"
                + " add {6} sub.\n");
        expected.clear();
        expected.accept(createStack(mc, zero));
        assertCollectorsEqual(expected, results);

        // A branching expression.
        results.clear();
        qp.accept("(1 2) each. 3 add.\n");
        expected.clear();
        expected.accept(createStack(mc, four));
        expected.accept(createStack(mc, five));
        assertCollectorsEqual(expected, results);

        qp.close();
        //mc.close();
    }

    @Test
    public void testFuzz() throws Exception {
        Model model = getTestModel();
        StackEvaluator eval = new LazyStackEvaluator();

        // Discard error output (of which there will be a lot).
        PrintStream errStream = new PrintStream(new NullOutputStream());

        QueryEngine qe = new QueryEngine(model, eval, System.out, errStream);
        Collector<RippleList> expected = new Collector<>();
        Collector<RippleList> results = new Collector<>();
        QueryPipe qp = new QueryPipe(qe, results);
        ModelConnection mc = qe.getConnection();

        Number five = 5;

        byte[] bytes = new byte[128];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (i >= 32)
                    ? (byte) i
                    : (i >= 16)
                    ? (byte) '\n'
                    : (byte) '\t';
        }

        for (int i = 0; i < REPEAT; i++) {
            // Feed the pipe some fuzz.
            int len = MAX_EXPR_LENGTH + rand.nextInt(MAX_EXPR_LENGTH - MIN_EXPR_LENGTH);
            byte[] expr = new byte[len];
            for (int j = 0; j < len; j++) {
                expr[j] = bytes[rand.nextInt(bytes.length)];
            }
            qp.accept(new String(expr));

            qp.accept(".\n");
            qp.accept(".\n");
            qp.accept(".\n");

            // Now make sure the pipe still works.
            results.clear();
            qp.accept("2 3 add.\n");
            expected.clear();
            expected.accept(createStack(mc, five));
            assertCollectorsEqual(expected, results);
        }

        qp.close();
    }
}
