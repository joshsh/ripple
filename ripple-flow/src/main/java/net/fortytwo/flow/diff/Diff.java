package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

import java.util.LinkedList;

// TODO: add a concept of Source
/**
 * A two-channel data pipeline which distinguishes between data items "added" and "removed"
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Diff<T> implements DiffSink<T>, DiffSource<T> {
    private enum Action {Add, Remove}

    private class Change {
        public T value;
        public Action action;
    }

    // An order-preserving list of changes.
    private final LinkedList<Change> changes;

    private final Sink<T> plusSink;
    private final Sink<T> minusSink;

    public Diff() {
        changes = new LinkedList<>();

        plusSink = t -> {
            Change ch = new Change();
            ch.value = t;
            ch.action = Action.Add;
            changes.addLast(ch);
        };

        minusSink = t -> {
            Change ch = new Change();
            ch.value = t;
            ch.action = Action.Remove;
            changes.addLast(ch);
        };
    }

    public Sink<T> getPlus() {
        return plusSink;
    }

    public Sink<T> getMinus() {
        return minusSink;
    }


    public void clear() {
        changes.clear();
    }

    public int size() {
        return changes.size();
    }

    public void writeTo(final DiffSink<T> sink) throws RippleException {
        Sink<T> otherPlusSink = sink.getPlus();
        Sink<T> otherMinusSink = sink.getMinus();

        for (Change ch : changes) {
            switch (ch.action) {
                case Add:
                    otherPlusSink.accept(ch.value);
                    break;
                case Remove:
                    otherMinusSink.accept(ch.value);
                    break;
                default:
                    // FIXME
                    System.err.println("unsupported Action: " + ch.action);
            }
        }
    }
}
