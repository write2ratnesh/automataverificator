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
//    private long childVisit = 0;
//    private int childDepth = 0;
//    private int maxChildDepth = 0;

    private DfsStackTree<IntersectionNode> stackTree;
    //current dfs stack tree node
    private DfsStackTreeNode<IntersectionNode> stackTreeNode;

    private final Set<IntersectionNode> visited;

    private final ISharedData sharedData;
    private final int threadId;

    public ConcurrentMainDfs(ISharedData sharedData, DfsStackTree<IntersectionNode> stackTree, int threadId) {
        this.sharedData = sharedData;
        this.visited = sharedData.getVisited();
        this.threadId = threadId;
        this.stackTree = stackTree;
        this.stackTreeNode = stackTree.getRoot();
    }

    protected boolean leaveNode() {
        IntersectionNode node = stackTreeNode.getItem();
        if (stackTreeNode.wasLeft.compareAndSet(false, true)) {
            stackTreeNode.remove();

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
        if (node != stackTreeNode.getItem()) {
            throw new IllegalArgumentException();
        }

        visited.add(node);
        node.addOwner(threadId);
        while (stackTreeNode != null && sharedData.getContraryInstance() == null) {
            IntersectionNode child = stackTreeNode.getItem().next(-1);
            if (child != null) {
                if (!visited.contains(child)) {
                    if (visited.add(child)) {
                        stackTreeNode = stackTree.addChild(stackTreeNode, child);
                        stackTreeNode.getItem().addOwner(threadId);
                    }
                }
            } else {
                boolean flag = true;
                if (stackTreeNode.hasChildren()) {
                    for (DfsStackTreeNode<IntersectionNode> childNode: stackTreeNode.getChildren()) {
                        if (!childNode.wasLeft.get()) {
                            //TODO
//                            childVisit++;
//                            childDepth++;
                            //----------

                            flag = false;
                            stackTreeNode = childNode;
                            stackTreeNode.getItem().addOwner(threadId);
                            break;
                        }
                    }
                }
                if (flag) {
                    if (leaveNode()) {
                        break;
                    }
//                    if (childDepth > 0) {
//                        maxChildDepth = Math.max(childDepth, maxChildDepth);
//                        childDepth--;
//                    }
                    stackTreeNode.getItem().removeOwner(threadId);
                    stackTreeNode = stackTreeNode.getParent();
                }
            }
        }
//        System.out.println("Child visit: " + childVisit);
//        System.out.println("Child depth: " + maxChildDepth);
        return null;
    }
}
