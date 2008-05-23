/**
 * DfsThread.java, 09.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.util.DequeSet;
import ru.ifmo.util.CollectionUtils;

import java.util.Deque;
import java.util.Map;
import java.io.PrintStream;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class DfsThread extends Thread {

    private volatile IntersectionNode initial;
    private Deque<IntersectionNode> stack = new DequeSet<IntersectionNode>();
    private ISharedData sharedData;

    public DfsThread(IntersectionNode initial, ISharedData sharedData) {
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
        try {
            for (ConcurrentMainDfs dfs = new ConcurrentMainDfs(sharedData, stack, getId());
                    dfs.getStack().isEmpty() && (sharedData.getContraryInstance() == null);) {
                dfs.dfs(initial);
                if (sharedData.getContraryInstance() == null) {
                    initial = null;
                    stack = null;
                    if (!sharedData.offerUnoccupiedThread(this)) {
                        sharedData.setContraryInstance(CollectionUtils.<IntersectionNode>emptyDeque());
                        sharedData.notifyAllUnoccupiedThreads();
                        return;
                    }
                    synchronized (this) {
                        while (initial == null && sharedData.getContraryInstance() == null) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            printAllStacks(System.err);
            t.printStackTrace();
        }
    }

    private static void printAllStacks(PrintStream s) {
        Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();

        synchronized (s) {
            for (Map.Entry<Thread, StackTraceElement[]> entry: stacks.entrySet()) {
                s.println("Thread " + entry.getKey().getName());
                for (StackTraceElement trace: entry.getValue()) {
                    s.println("\tat " + trace);
                }
            }
            s.println();
        }
    }
}
