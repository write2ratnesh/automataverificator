/**
 * SharedData.java, 16.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.util.concurrent.DfsStackTreeNode;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

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
    private AtomicReference<DfsStackTreeNode<IntersectionNode>> contraryInstance =
            new AtomicReference<DfsStackTreeNode<IntersectionNode>>();

    /**
     * Visited nodes. Sould be concurrent or synchronized set when is used sumultaneusly.
     */
    private final Set<IntersectionNode> visited;

    public SharedData(Set<IntersectionNode> visited, int threadNumber) {
        if (visited == null) {
            throw new IllegalArgumentException();
        }
        this.visited = visited;
        this.threadNumber = threadNumber;
    }

    public DfsStackTreeNode<IntersectionNode> getContraryInstance() {
        return contraryInstance.get();
    }

    public boolean setContraryInstance(DfsStackTreeNode<IntersectionNode> contraryInstance) {
        return this.contraryInstance.compareAndSet(null, contraryInstance);
    }

    public Set<IntersectionNode> getVisited() {
        return visited;
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
