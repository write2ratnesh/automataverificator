/**
 * AbstractDfs.java, 12.04.2008
 */
package ru.ifmo.verifier.impl;

import ru.ifmo.verifier.automata.IntersectionNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractDfs<R> {

    private Deque<IntersectionNode> stack = new LinkedList<IntersectionNode>();
    private R result;

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
        Set<IntersectionNode> visited = new HashSet<IntersectionNode>();

        enterNode(node);
        visited.add(node);
        stack.push(node);
        while (!stack.isEmpty()) {
            IntersectionNode n = stack.getFirst();
            IntersectionNode child = n.next();
            if (child != null) {
                if (visitNode(child)) {
                    break;
                }
                if (!visited.contains(child)) {
                    enterNode(child);
                    visited.add(child);
                    stack.push(child);
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
