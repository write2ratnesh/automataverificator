/**
 * AbstractDfs.java, 12.04.2008
 */
package ru.ifmo.verifier;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.util.DequeSet;

import java.util.Deque;
import java.util.Set;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public abstract class AbstractDfs<R> implements IDfs<R> {
    private final Deque<IntersectionNode> stack = new DequeSet<IntersectionNode>();
    
    private final Set<IntersectionNode> visited;
    private R result;

    protected final ISharedData sharedData;
    protected final int threadId;

    public AbstractDfs(ISharedData sharedData, Set<IntersectionNode> visited, int threadId) {
        this.sharedData = sharedData;
        this.visited = visited;
        this.threadId = threadId;
    }

    protected void enterNode(IntersectionNode node) {
    }

    protected boolean visitNode(IntersectionNode node) {
        return false;
    }

    protected boolean leaveNode(IntersectionNode node) {
        return false;
    }

    protected Deque<IntersectionNode> getStack() {
        return stack;
    }

    protected void setResult(R result) {
        this.result = result;
    }

    public R dfs(IntersectionNode node) {
        stack.clear();

        enterNode(node);
        visited.add(node);
        stack.push(node);
        while (!stack.isEmpty() && sharedData.getContraryInstance() == null) {
            IntersectionNode n = stack.getFirst();
            IntersectionNode child = n.next(threadId);
            if (child != null) {
                if (visitNode(child)) {
                    break;
                }
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
        return result;
    }
}
