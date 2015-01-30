package net.fortytwo.ripple.cli;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.util.ModelConnectionHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A <code>Sink</code> which accepts Ripple stacks and prints them to a
 * specified stream in LIFO order, including a Turtle-styled tree view for the
 * topmost item in the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TurtleView implements Sink<RippleList> {
    // A three-space-indented tree seems to be the most readable.
    private static final String INDENT = "   ";

    private static final String INDEX_SEPARATOR = "  ";

    private final RipplePrintStream printStream;
    private final ModelConnection modelConnection;
    private final ModelConnectionHelper helper;

    private final boolean printEntireStack;
    private final boolean showEdges;
    private final int maxPredicates;
    private final int maxObjects;
    private final boolean deduplicateObjects;

    private int index = 0;

    public TurtleView(final RipplePrintStream printStream,
                      final ModelConnection modelConnection)
            throws RippleException {
        this.printStream = printStream;
        this.modelConnection = modelConnection;
        this.helper = new ModelConnectionHelper(modelConnection);

        RippleProperties props = Ripple.getConfiguration();
        this.printEntireStack = props.getBoolean(
                Ripple.RESULT_VIEW_PRINT_ENTIRE_STACK);
        this.showEdges = props.getBoolean(
                Ripple.RESULT_VIEW_SHOW_EDGES);
        //&& modelConnection.getModel() instanceof SesameModel;
        this.maxPredicates = props.getInt(
                Ripple.RESULT_VIEW_MAX_PREDICATES);
        this.maxObjects = props.getInt(
                Ripple.RESULT_VIEW_MAX_OBJECTS);
        this.deduplicateObjects = props.getBoolean(
                Ripple.RESULT_VIEW_DEDUPLICATE_OBJECTS);
    }

    public int size() {
        return index;
    }

    /**
     * Accepts a stack, printing it to the specified stream.
     *
     * @param stack the stack to print.  May not be a nil list.
     * @throws RippleException if something goes awry
     */
    public void put(final RippleList stack) throws RippleException {
        // Grab the topmost item on the stack.
        Object subject = stack.getFirst();

        // View the list in right-to-left order
        RippleList list = stack.invert();

        String prefix = "  [" + ++index + "]" + INDEX_SEPARATOR;
        String prefixIndent = "                ".substring(0, prefix.length());
        printStream.print(prefix);
        //printStream.print( "" + ++index + " ->" + INDEX_SEPARATOR );
        //printStream.print( "rdf:_" + ++index + INDEX_SEPARATOR );

        if (printEntireStack) {
            list.printTo(printStream, modelConnection, false);
        } else {
            modelConnection.print(subject, printStream);
        }

        printStream.print("\n");

        if (showEdges && !stack.isNil()) {
            Collector<Object> predicates = new Collector<Object>();
            helper.findPredicates(subject, predicates);

            int predCount = 0;

            for (Iterator<Object> predIter = predicates.iterator();
                 predIter.hasNext(); ) {
                printStream.print(prefixIndent);
                printStream.print(INDENT);

                // Stop after maxPredicates predicates have been displayed, unless
                // maxPredicates < 0, which indicates an unlimited number of
                // predicates.
                if (maxPredicates >= 0 && ++predCount > maxPredicates) {
                    printStream.print("[...]\n");
                    break;
                }

                Object predicate = predIter.next();
                modelConnection.print(predicate, printStream);
                printStream.print("\n");

                Collector<Object> objects = new Collector<Object>();
                StatementPatternQuery query = new StatementPatternQuery(subject, predicate, null);
                modelConnection.query(query, objects, false);

                int objCount = 0;

                Collection<Object> objColl;
                if (deduplicateObjects) {
                    objColl = new HashSet<Object>();
                    objColl.addAll(objects);
                } else {
                    objColl = objects;
                }

                for (Iterator<Object> objIter = objColl.iterator();
                     objIter.hasNext(); ) {
                    printStream.print(prefixIndent);
                    printStream.print(INDENT);
                    printStream.print(INDENT);

                    // Stop after maxObjects objects have been displayed, unless
                    // maxObjects < 0, which indicates an unlimited number of
                    // objects.
                    if (maxObjects >= 0 && ++objCount > maxObjects) {
                        printStream.print("[...]\n");
                        break;
                    }

                    Object object = objIter.next();
                    modelConnection.print(object, printStream);
                    printStream.print((objIter.hasNext())
                            ? ","
                            : (predIter.hasNext())
                            ? ";" : ".");
                    printStream.print("\n");
                }
            }
        }
    }
}
