/**
 * MainDfs.java, 09.05.2008
 */
package ru.ifmo.verifier.impl;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.AbstractDfs;
import ru.ifmo.verifier.concurrent.SharedData;
import ru.ifmo.util.CollectionUtils;

import java.util.Deque;
import java.util.HashSet;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class MainDfs extends AbstractDfs<Deque<IntersectionNode>> {
    private long curThreadId;
    
    public MainDfs(SharedData sharedData, long curThreadId) {
        super(sharedData, sharedData.visited, 0);
        setResult(CollectionUtils.<IntersectionNode>emptyDeque());
        this.curThreadId = curThreadId;
    }

    protected boolean leaveNode(IntersectionNode node) {
        assert node.next(threadId) == null;
        if (node.isTerminal()) {
            AbstractDfs<Boolean> dfs2 = new SecondDfs(sharedData, new HashSet<IntersectionNode>(), getStack(), curThreadId);
            if (dfs2.dfs(node)) {
                setResult(getStack());
                return true;
            }
        }
        return false;
    }
}
