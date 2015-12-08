package net.fortytwo.ripple.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

/**
 * A set of tasks which are executed concurrently.  Tasks are scheduled for
 * execution as soon as they are added to the set.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TaskSet {
    private int count = 0;

    private final Sink<Task> completedTaskSink = new Sink<Task>() {
        public synchronized void put(final Task task) throws RippleException {
            count--;

            if (0 == count) {
                notify();
            }
        }
    };

    public void add(final Task task) throws RippleException {
        synchronized (completedTaskSink) {
            count++;
        }

        Scheduler.add(task, completedTaskSink);
    }

    /**
     * Note: all tasks are to have been added to the set before this method is
     * called, while there may be any number of tasks which have not finished
     * executing.
     */
    public void waitUntilEmpty() throws RippleException {
        synchronized (completedTaskSink) {
            if (count > 0) {
                try {
                    completedTaskSink.wait();
                } catch (java.lang.InterruptedException e) {
                    throw new RippleException("interrupted while waiting to complete tasks");
                }
            }
        }
    }

    public void stopWaiting() {
        synchronized (completedTaskSink) {
            completedTaskSink.notify();
        }
    }
}

