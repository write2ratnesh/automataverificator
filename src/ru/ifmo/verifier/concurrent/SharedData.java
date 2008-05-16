/**
 * SharedData.java, 16.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;

import java.util.Deque;
import java.util.Set;

/**
 * Shared data for all simultaneous threads
 *
 * @author: Kirill Egorov
 */
public class SharedData {

    /**
     * Thread checked not emptytness of automatas intersection,
     * write his cotrary instance stack and other threads can terminate their execution.
     */
    public volatile Deque<IntersectionNode> contraryInstance = null;

    /**
     * Visited nodes. Sould be concurrent set or synchronized when is used sumultaneusly.
     */
    public final Set<IntersectionNode> visited;

    public SharedData(Set<IntersectionNode> visited) {
        if (visited == null) {
            throw new IllegalArgumentException();
        }
        this.visited = visited;
    }
}
