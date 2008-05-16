/**
 * DfsThread.java, 09.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.impl.MainDfs;

import java.util.Deque;
import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class DfsThread extends Thread {

    private IntersectionNode initial;
    private Deque<IntersectionNode> result;
    private SharedData sharedData;

    public DfsThread(IntersectionNode initial, SharedData sharedData) {
        super();
        if (sharedData == null) {
            throw new IllegalArgumentException();
        }
        this.sharedData = sharedData;
        this.initial = initial;
    }

    public void setInitial(IntersectionNode initial) {
        this.initial = initial;
    }

    public Deque<IntersectionNode> getResult() {
        return result;
    }

    public void run() {
        if (initial == null) {
            throw new RuntimeException("Initial node hasn't been initialized yet");
        }
        result = new MainDfs(sharedData, getId()).dfs(initial);
    }
}
