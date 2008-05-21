/**
 * SharedData.java, 16.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;

import java.util.Deque;
import java.util.Set;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    /**
     * Thread checked not emptytness of automatas intersection,
     * write his cotrary instance stack and other threads can terminate their execution.
     */
    public volatile Deque<IntersectionNode> contraryInstance = null;

    /**
     * Visited nodes. Sould be concurrent or synchronized set when is used sumultaneusly.
     */
    public final Set<IntersectionNode> visited;

    public SharedData(Set<IntersectionNode> visited) {
        if (visited == null) {
            throw new IllegalArgumentException();
        }
        this.visited = visited;
    }

    public void addUnoccupiedThread(DfsThread t) {
        unoccupiedThreads.add(t);
    }

    /**
     * Get unoccupied thread and remove it from waiting threads
     * @return unoccupied thread or null, if no waiting thread is exist
     */
    public DfsThread getUnoccupiedThread() {
        return unoccupiedThreads.poll();
    }
}
