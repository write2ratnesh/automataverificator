/*
 * Developed by eVelopers Corporation - 21.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.IDfs;
import ru.ifmo.verifier.AbstractDfs;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.verifier.impl.SecondDfs;
import ru.ifmo.util.DequeSet;

import java.util.*;

public class ConcurrentMainDfs implements IDfs<Void> {
    private final Deque<IntersectionNode> stack;

    private final Set<IntersectionNode> visited;

    private final ISharedData sharedData;
    private final long threadId;

    public ConcurrentMainDfs(ISharedData sharedData, long threadId) {
        this(sharedData, new DequeSet<IntersectionNode>(), threadId);
    }

    public ConcurrentMainDfs(ISharedData sharedData, Deque<IntersectionNode> stack, long threadId) {
        this.sharedData = sharedData;
        this.visited = sharedData.getVisited();
        this.threadId = threadId;
        this.stack = stack;
    }

    protected void enterNode(IntersectionNode node) {
        DfsThread t = sharedData.getUnoccupiedThread();
        if (t != null) {
            Deque<IntersectionNode> subStack = new DequeSet<IntersectionNode>(stack.size());
            IntersectionNode initial = null;

            // Clone stack part without children.
            for (IntersectionNode n: stack) {
                subStack.add(n);
                IntersectionNode child = n.next(0);
                if (child != null && !visited.contains(child)) {
                    initial = child;
                    break;
                }
            }
            if (initial != null) {
                // Notify waiting thread
                synchronized (t) {
                    t.setInitial(initial);
                    t.setInitialStack(subStack);
                    t.notifyAll();
                }
            } else {
                if (!sharedData.offerUnoccupiedThread(t)) {
                    throw new RuntimeException("Unextpected count of waiting threads");
                }
            }
        }
    }

    protected boolean leaveNode(IntersectionNode node) {
        assert node.next(0) == null;
        if (node.isTerminal()) {
            AbstractDfs<Boolean> dfs2 = new SecondDfs(sharedData, getStack(), threadId);
            if (dfs2.dfs(node)) {
                return true;
            }
        }
        return false;
    }

    public Deque<IntersectionNode> getStack() {
        return stack;
    }

    public Void dfs(IntersectionNode node) {
        stack.push(node);
        visited.add(node);
        while (!stack.isEmpty() && sharedData.getContraryInstance() == null) {
            IntersectionNode n = stack.getFirst();
            IntersectionNode child = n.next(0);
            if (child != null) {
                if (!visited.contains(child)) {
                    if (visited.add(child)) {
                        stack.push(child);
                        enterNode(child);
                    }
                }
            } else {
                if (leaveNode(n)) {
                    break;
                }
                stack.pop();
            }
        }
        return null;
    }
}
