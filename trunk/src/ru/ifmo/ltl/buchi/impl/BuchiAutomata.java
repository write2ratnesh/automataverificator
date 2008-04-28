/**
 * BuchiAutomata.java, 16.03.2008
 */
package ru.ifmo.ltl.buchi.impl;

import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.IBuchiNode;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class BuchiAutomata implements IBuchiAutomata {

    private IBuchiNode startNode;
    private Set<IBuchiNode> nodes = new HashSet<IBuchiNode>();
    private Map<Integer, Set<IBuchiNode>> accept = new HashMap<Integer, Set<IBuchiNode>>();
    private int acceptSetsCount = 0;

    public IBuchiNode getStartNode() {
        return startNode;
    }

    public void setStartNode(IBuchiNode startNode) {
        this.startNode = startNode;
    }

    public Set<IBuchiNode> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<IBuchiNode> getAcceptSet(int i) {
        if ((i < 0) || (i >= acceptSetsCount)) {
            throw new IndexOutOfBoundsException("Should be 0 <= i < acceptSetsCount");
        }
        return accept.get(i);
    }

    public int getAcceptSetsCount() {
        return acceptSetsCount;
    }

    public void addAcceptSet(Set<IBuchiNode> acceptSet) {
        if (acceptSet == null || acceptSet.isEmpty()) {
            throw new IllegalArgumentException("acceptSet can't be null or emty");
        }
        accept.put(acceptSetsCount++, acceptSet);
    }

    public int size() {
        return nodes.size();
    }

    public void addNode(IBuchiNode node) {
        nodes.add(node);
    }

    public void addNodes(Collection<? extends IBuchiNode> nodes) {
        for (IBuchiNode n: nodes) {
            addNode(n);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append("start ");
        buf.append(startNode.getID());
        buf.append("\n");
        for (IBuchiNode n: nodes) {
            buf.append(n);
        }
        for (Map.Entry<Integer, Set<IBuchiNode>> entry: accept.entrySet()) {
            buf.append(String.format("Accept set %d [", entry.getKey()));
            for (IBuchiNode node: entry.getValue()) {
                buf.append(node.getID()).append(", ");
            }
            buf.replace(buf.length() - 2, buf.length(), "]\n");
        }
        return buf.toString();
    }

}
