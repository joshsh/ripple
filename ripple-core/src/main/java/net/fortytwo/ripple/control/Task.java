package net.fortytwo.ripple.control;

import net.fortytwo.ripple.RippleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class Task {
    private static final Logger logger = LoggerFactory.getLogger(Task.class.getName());

    private LinkedList<Task> children = null;
    private boolean finished = true, stopped = false;

    protected abstract void executeProtected() throws RippleException;

    protected abstract void stopProtected();

    public void execute() throws RippleException {
//System.out.println( "[" + this + "].execute()" );
        // A task may be stopped before it executes.
        if (!stopped) {
            executeProtected();
        }
//else
//System.out.println( "    already stopped!" );

        synchronized (this) {
            finished = true;

            notify();
        }
    }

    /**
     * Note: it is possible to stop a task which has already finished executing
     * (the effect is to stop any children which may still be executing).
     */
    public synchronized void stop() {
//System.out.println( "[" + this + "].stop()" );
        if (!stopped) {
            stopped = true;

            stopProtected();

            if (null != children) {
                //System.out.println( "    stopping child: " + child );
                children.forEach(net.fortytwo.ripple.control.Task::stop);
            }
        }
    }

    /**
     * Note: should not be called outside of Scheduler.
     */
    public void begin() {
        finished = false;
        stopped = false;

        // Note: while executing a task a second time is permitted, the task loses
        // ownership of any children acquired through a previous execution.
        if (null != children) {
            children.clear();
        }
    }

    /**
     * Adds a child task.
     * Note: should not be called outside of Scheduler.
     *
     * @param child the task to add
     */
    public synchronized void addChild(final Task child) {
//System.out.println( "[" + this + "].addChild(" + child + ")" );
        if (finished) {
            logger.error("attempted to add a child to a finished task");
        } else {
            if (null == children) {
                children = new LinkedList<>();
            }

            children.add(child);
        }
    }

    /**
     * Blocks until this task has finished.
     *
     * @throws RippleException if things go astray
     */
    public void waitUntilFinished() throws RippleException {
//System.out.println( "[" + this + "].waitUntilFinished()" );
        synchronized (this) {
            if (!finished) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RippleException("interrupted while waiting for task to finish");
                }
            }
        }
//System.out.println( "    done -- " + this );
    }
}

