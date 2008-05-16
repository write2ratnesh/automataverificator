/**
 * AbstractDfs.java, 12.04.2008
 */
package ru.ifmo.verifier;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.concurrent.SharedData;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractDfs<R> {
    //TODO: change deque to something implemented Deque and Set
    private Deque<IntersectionNode> stack = new LinkedList<IntersectionNode>();
    
    private Set<IntersectionNode> visited;
    private R result;

    protected SharedData sharedData;
    protected long threadId;

    public AbstractDfs(SharedData sharedData, Set<IntersectionNode> visited, long threadId) {
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
        while (!stack.isEmpty() && sharedData.contraryInstance == null) {
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
