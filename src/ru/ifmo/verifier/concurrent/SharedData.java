/**
 * SharedData.java, 16.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Shared data for all simultaneous threads
 *
 * @author: Kirill Egorov
 */
public class SharedData {
    /**
     * Threads that finished their work and waiting for new one
     */
    private final Queue<DfsThread> unoccupiedThreads = new ConcurrentLinkedQueue<DfsThread>();

    private final int threadNumber;

    /**
     * Thread checked not emptytness of automatas intersection,
     * write his cotrary instance stack and other threads can terminate their execution.
     */
    public volatile Deque<IntersectionNode> contraryInstance = null;

    /**
     * Visited nodes. Sould be concurrent or synchronized set when is used sumultaneusly.
     */
    public final Set<IntersectionNode> visited;

    public SharedData(Set<IntersectionNode> visited, int threadNumber) {
        if (visited == null) {
            throw new IllegalArgumentException();
        }
        this.visited = visited;
        this.threadNumber = threadNumber;
    }

    /**
     * Insert thread into queue
     * @param t thread to be put into the queue
     * @return false if all threads are waiting.
     */
    public boolean offerUnoccupiedThread(DfsThread t) {
        return unoccupiedThreads.offer(t) && (unoccupiedThreads.size() < threadNumber);
    }

    /**
     * Get unoccupied thread and remove it from waiting threads
     * @return unoccupied thread or null, if no waiting thread is exist
     */
    public DfsThread getUnoccupiedThread() {
        return unoccupiedThreads.poll();
    }

    public void notifyAllUnoccupiedThreads() {
        if (unoccupiedThreads != null) {
            for (DfsThread t: unoccupiedThreads) {
                synchronized (t) {
                    t.notifyAll();
                }
            }
        }
    }
}
