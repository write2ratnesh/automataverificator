/**
 * DfsThread.java, 09.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.util.DequeSet;
import ru.ifmo.util.CollectionUtils;

import java.util.Deque;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class DfsThread extends Thread {

    private volatile IntersectionNode initial;
    private Deque<IntersectionNode> stack = new DequeSet<IntersectionNode>();
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

    public void setInitialStack(Deque<IntersectionNode> stack) {
        if (stack == null) {
            throw new IllegalArgumentException();
        }
        this.stack = stack;
    }

    public void run() {
        if (initial == null) {
            throw new RuntimeException("Initial node hasn't been initialized yet");
        }
        for (ConcurrentMainDfs dfs = new ConcurrentMainDfs(sharedData, stack, getId());
                dfs.getStack().isEmpty() && (sharedData.contraryInstance == null);) {
            dfs.dfs(initial);
            if (sharedData.contraryInstance == null) {
                initial = null;
                stack = null;
                if (!sharedData.offerUnoccupiedThread(this)) {
                    sharedData.contraryInstance = CollectionUtils.emptyDeque();
                    sharedData.notifyAllUnoccupiedThreads();
                    return;
                }
                synchronized (this) {
                    while (initial == null && sharedData.contraryInstance == null) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
