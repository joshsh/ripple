package net.fortytwo.ripple.control;

import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private static Scheduler singleInstance = null;
    private static long workerThreadCount = 0;

    private final LinkedList<TaskItem> taskQueue;
    private final LinkedList<WorkerRunnable> allRunnables;
    private final LinkedList<WorkerRunnable> waitingRunnables;
    private final int maxThreads;

    public static void add(final Task task, final Sink<Task> completedTaskSink) throws RippleException {
        if (null == singleInstance) {
            singleInstance = new Scheduler();
        }

        singleInstance.addPrivate(task, completedTaskSink);
    }

    public static void add(final Task task) throws RippleException {
        add(task, new NullSink<>());
    }

    private synchronized static long nextWorkerId() {
        return ++workerThreadCount;
    }

    private Scheduler() throws RippleException {
        taskQueue = new LinkedList<>();
        allRunnables = new LinkedList<>();
        waitingRunnables = new LinkedList<>();

        maxThreads = Ripple.getConfiguration().getInt(Ripple.MAX_WORKER_THREADS);
    }

    private void addPrivate(final Task task, final Sink<Task> completedTaskSink) {
        // Initialize the task immediately.  It may not begin executing for
        // some time.
        task.begin();

        // Add the new task as a child of the currently executing task.
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof WorkerThread) {
            Task parent = ((WorkerThread) currentThread).getCurrentTask();
            parent.addChild(task);
        }

        TaskItem taskItem = new TaskItem(task, completedTaskSink);

        synchronized (taskQueue) {
            taskQueue.addLast(taskItem);

            // When the queue goes from empty to nonempty, there may be a number
            // of threads waiting for a task.  Notify the first in line that
            // a task is available.
            if (1 == taskQueue.size() && waitingRunnables.size() > 0) {
                WorkerRunnable r = waitingRunnables.removeFirst();

                // Remove a task from the queue immediately.
                r.retrieveTask();

                // Alert the waiting runnable that it now has a task.
                synchronized (r) {
                    r.notify();
                }
            }

            // If there are more tasks than threads, and we have not reached the
            // maximum number of threads, then start a new one.
            else if (allRunnables.size() < maxThreads) {
                WorkerRunnable r = new WorkerRunnable();
                allRunnables.add(r);
                Thread t = new WorkerThread(r);
                t.start();
            }
        }
    }

    // has not been tested
    public void stopAll() {
        synchronized (allRunnables) {
            for (WorkerRunnable r : allRunnables) {
                Task task = r.getCurrentTask();
                if (null != task) {
                    task.stop();
                }
            }
        }
    }

    private class TaskItem {
        public final Task task;
        public final Sink<Task> sink;

        public TaskItem(final Task task, final Sink<Task> sink) {
            this.task = task;
            this.sink = sink;
        }
    }

    private class WorkerThread extends Thread {
        private final WorkerRunnable runnable;
        private final long id;

        public WorkerThread(final WorkerRunnable r) {
            super(r);
            id = nextWorkerId();
            this.setName("Ripple worker thread #" + id);

            runnable = r;
        }

        public Task getCurrentTask() {
            return runnable.getCurrentTask();
        }
    }

    private class WorkerRunnable implements Runnable {
        private TaskItem currentTaskItem = null;

        public void run() {
            // Continue waiting for and executing tasks indefinitely.
            while (true) {
                if (null == currentTaskItem) {
                    // Try to remove a task from the queue.
                    synchronized (taskQueue) {
                        if (taskQueue.size() > 0) {
                            currentTaskItem = taskQueue.removeFirst();
                        }
                    }
                }

                // If a task was found in the queue, execute it.
                if (null != currentTaskItem) {
                    try {
                        currentTaskItem.task.execute();
                    }

                    // This is the end of the line for exceptions.
                    catch (Throwable t) {
                        logThrowable(t);
                    }

                    // Even tasks which failed with a throwable are put into
                    // the appropriate completed task sink.
                    try {
                        currentTaskItem.sink.accept(currentTaskItem.task);
                    } catch (Throwable t) {
                        logThrowable(t);
                    }

                    currentTaskItem = null;
                }

                // If there are no tasks in the queue, add this Runnable to a
                // list and wait.
                else {
                    synchronized (taskQueue) {
                        waitingRunnables.addLast(this);
                    }

                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            logger.warn("worker runnable interrupted while waiting for new tasks");
                        }
                    }
                }
            }
        }

        private void logThrowable(final Throwable t) {
            if (t instanceof InterruptedException) {
                logger.warn("task interrupted: " + currentTaskItem.task);
            } else {
                logger.error("exception in scheduler", t);
            }
        }

        public void retrieveTask() {
            currentTaskItem = taskQueue.removeFirst();
        }

        public Task getCurrentTask() {
            return currentTaskItem.task;
        }
    }
}

