/*
 * Developed by eVelopers Corporation - 21.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.IDfs;
import ru.ifmo.verifier.AbstractDfs;
import ru.ifmo.verifier.impl.SecondDfs;
import ru.ifmo.util.DequeSet;

import java.util.*;

public class ConcurrentMainDfs implements IDfs<Void> {
    private final Deque<IntersectionNode> stack;

    private final Set<IntersectionNode> visited;

    private final SharedData sharedData;
    private final long threadId;

    public ConcurrentMainDfs(SharedData sharedData, long threadId) {
        this(sharedData, new DequeSet<IntersectionNode>(), threadId);
    }

    public ConcurrentMainDfs(SharedData sharedData, Deque<IntersectionNode> stack, long threadId) {
        this.sharedData = sharedData;
        this.visited = sharedData.visited;
        this.threadId = threadId;
        this.stack = stack;
    }

    protected void enterNode(IntersectionNode node) {
        DfsThread t = sharedData.getUnoccupiedThread();
        if (t != null) {
            /*
             * 1. Clone stack for waiting thread.
             * 2. Set cloned thread to waiting thread and change initial state.
             * 3. Notify waiting thread.
             */
            Deque<IntersectionNode> cloneStack = new DequeSet<IntersectionNode>(stack.size() * 2);
            cloneStack.addAll(stack);
            IntersectionNode n = cloneStack.pop();
            assert n == node;
            t.setInitial(node);
            t.setInitialStack(cloneStack);

            synchronized (t) {
                t.notify();
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
        while (!stack.isEmpty() && sharedData.contraryInstance == null) {
            IntersectionNode n = stack.getFirst();
            IntersectionNode child = n.next(threadId);
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
