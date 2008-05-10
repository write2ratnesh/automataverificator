/**
 * MainDfs.java, 09.05.2008
 */
package ru.ifmo.verifier.impl;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.AbstractDfs;

import java.util.Set;
import java.util.Deque;
import java.util.HashSet;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class MainDfs extends AbstractDfs<Deque<IntersectionNode>> {
    private long curThreadId;
    
    public MainDfs(Set<IntersectionNode> visited, long curThreadId) {
        super(visited, 0);
        setResult(getStack());
        this.curThreadId = curThreadId;
    }

    protected boolean leaveNode(IntersectionNode node) {
        assert node.next(threadId) == null;
        if (node.isTerminal()) {
            AbstractDfs<Boolean> dfs2 = new SecondDfs(getStack(), new HashSet<IntersectionNode>(), curThreadId);
            if (dfs2.dfs(node)) {
                return true;
            }
        }
        return false;
    }
}
