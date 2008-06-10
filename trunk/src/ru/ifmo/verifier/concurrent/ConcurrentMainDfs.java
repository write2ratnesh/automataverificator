/*
 * Developed by eVelopers Corporation - 21.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.IDfs;
import ru.ifmo.verifier.AbstractDfs;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.util.concurrent.DfsStackTreeNode;
import ru.ifmo.util.concurrent.DfsStackTree;

import java.util.*;

public class ConcurrentMainDfs implements IDfs<Void> {
    private DfsStackTree<IntersectionNode> stackTree;
    //current dfs stack tree node
    private DfsStackTreeNode<IntersectionNode> stackTreeNode;

    private final Set<IntersectionNode> visited;

    private final ISharedData sharedData;
    private final long threadId;

    public ConcurrentMainDfs(ISharedData sharedData, DfsStackTree<IntersectionNode> stackTree, long threadId) {
        this.sharedData = sharedData;
        this.visited = sharedData.getVisited();
        this.threadId = threadId;
        this.stackTree = stackTree;
        this.stackTreeNode = stackTree.getRoot();
    }

    protected boolean leaveNode() {
//        System.out.println(threadId + " leave node: " + stackTreeNode.getItem());

        IntersectionNode node = stackTreeNode.getItem();
        if (stackTreeNode.wasLeft.compareAndSet(false, true)) {
            stackTreeNode.remove();
//            System.out.println(threadId + " remove node: " + stackTreeNode.getItem());

            if (node.isTerminal()) {
                AbstractDfs<Boolean> dfs2 = new ConcurrentSecondDfs(sharedData, stackTreeNode, threadId);
                if (dfs2.dfs(node)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Void dfs(IntersectionNode node) {
//        System.out.println(threadId + ": " + stackTreeNode.getItem());
        visited.add(node);
        while (stackTreeNode != null && sharedData.getContraryInstance() == null) {
            IntersectionNode child = stackTreeNode.getItem().next(0);
            if (child != null) {
                if (!visited.contains(child)) {
                    if (visited.add(child)) {
                        stackTreeNode = stackTree.addChild(stackTreeNode, child);
//                        System.out.println(threadId + ": " + stackTreeNode.getItem());
                    }
                }
            } else {
                boolean flag = true;
                if (stackTreeNode.hasChildren()) {
                    for (DfsStackTreeNode<IntersectionNode> childNode: stackTreeNode.getChildren()) {
                        if (!childNode.wasLeft.get()) {
                            flag = false;
                            stackTreeNode = childNode;
//                            System.out.println(threadId + " child: " + stackTreeNode.getItem());
                            break;
                        }
                    }
                }
                if (flag) {
                    if (leaveNode()) {
                        break;
                    }
                    stackTreeNode = stackTreeNode.getParent();
                }
            }
        }
        return null;
    }
}
