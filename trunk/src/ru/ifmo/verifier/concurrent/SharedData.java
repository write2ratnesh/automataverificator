/**
 * SharedData.java, 16.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.ISharedData;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shared data for all simultaneous threads
 *
 * @author: Kirill Egorov
 */
public class SharedData implements ISharedData {
    /**
     * Threads that finished their work and waiting for new one
     */
    private final Queue<DfsThread> unoccupiedThreads = new ConcurrentLinkedQueue<DfsThread>();

    /**
     * Number of threads
     */
    private final int threadNumber;

    /**
     * Thread checked not emptytness of automatas intersection,
     * write his cotrary instance stack and other threads can terminate their execution.
     */
    private volatile Deque<IntersectionNode> contraryInstance = null;

    /**
     * Visited nodes. Sould be concurrent or synchronized set when is used sumultaneusly.
     */
    private final Set<IntersectionNode> visited;

    /**
     * Minimum stack depth where node has next child
     */
    private int minStackDepth = Integer.MAX_VALUE;

    private AtomicBoolean allowStartThreads = new AtomicBoolean();

    public SharedData(Set<IntersectionNode> visited, int threadNumber) {
        if (visited == null) {
            throw new IllegalArgumentException();
        }
        this.visited = visited;
        this.threadNumber = threadNumber;
    }

    public Deque<IntersectionNode> getContraryInstance() {
        return contraryInstance;
    }

    public void setContraryInstance(Deque<IntersectionNode> contraryInstance) {
        this.contraryInstance = contraryInstance;
    }

    public Set<IntersectionNode> getVisited() {
        return visited;
    }

    public void setMinStackDepth(int minStackDepth, AtomicBoolean allowStartThreads) {
        if (this.minStackDepth > minStackDepth) {
            this.minStackDepth = minStackDepth;
            this.allowStartThreads.set(false);
            this.allowStartThreads = allowStartThreads;
            this.allowStartThreads.set(true);
        }

    }

    public boolean offerUnoccupiedThread(DfsThread t) {
        return unoccupiedThreads.offer(t) && (unoccupiedThreads.size() < threadNumber);
    }

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
