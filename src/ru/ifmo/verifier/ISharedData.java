package ru.ifmo.verifier;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.concurrent.DfsThread;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public interface ISharedData {
    Deque<IntersectionNode> getContraryInstance();

    void setContraryInstance(Deque<IntersectionNode> contraryInstance);

    Set<IntersectionNode> getVisited();

    void setMinStackDepth(int minStackDepth, AtomicBoolean allowStartThreads);

    /**
     * Insert thread into queue
     * @param t thread to be put into the queue
     * @return false if all threads are waiting.
     */
    boolean offerUnoccupiedThread(DfsThread t);

    /**
     * Get unoccupied thread and remove it from waiting threads
     * @return unoccupied thread or null, if no waiting thread is exist
     */
    DfsThread getUnoccupiedThread();

    void notifyAllUnoccupiedThreads();
}
