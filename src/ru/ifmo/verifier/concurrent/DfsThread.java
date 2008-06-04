/**
 * DfsThread.java, 09.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.util.concurrent.DfsStackTree;

import java.util.Map;
import java.io.PrintStream;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class DfsThread extends Thread {

    private DfsStackTree<IntersectionNode> stackTree;
    private ISharedData sharedData;

    public DfsThread(DfsStackTree<IntersectionNode> stackTree, ISharedData sharedData) {
        super();
        if (sharedData == null) {
            throw new IllegalArgumentException();
        }
        this.sharedData = sharedData;
        this.stackTree = stackTree;
    }

    public void setDfsStackTree(DfsStackTree<IntersectionNode> stackTree) {
        this.stackTree = stackTree;
    }

    public void run() {
        if (stackTree == null) {
            throw new RuntimeException("Initial stack tree node hasn't been initialized yet");
        }
        try {
            ConcurrentMainDfs dfs = new ConcurrentMainDfs(sharedData, stackTree, getId());
            dfs.dfs(stackTree.getRoot().getItem());
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
