/**
 * DfsNode.java, 12.04.2008
 */
package ru.ifmo.verifier.automata;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.INode;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.ltl.buchi.ITransitionCondition;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.verifier.IInterNode;

import java.util.*;

/**
 * Node received during state mashine and buchi automata intersection
 *
 * @author: Kirill Egorov
 */
public class IntersectionNode implements INode<IntersectionTransition>, IInterNode {
    private IntersectionAutomata automata;
    private final IState state;
    private final IBuchiNode node;
    private final int acceptSet;
    private final int nextAcceptSet;
    private final boolean terminal;

    Iterator<IntersectionNode> iterator;

    private Set<IntersectionTransition> transitions = new LinkedHashSet<IntersectionTransition>();

    public IntersectionNode(IntersectionAutomata automata, IState state, IBuchiNode node, int acceptSet) {
        if (state == null) {
            throw new IllegalArgumentException("Automata state can't be null");
        }
        if (node == null) {
            throw new IllegalArgumentException("Buchi node can't be null");
        }

        this.automata = automata;
        this.state = state;
        this.node = node;
        this.acceptSet = acceptSet;

        IBuchiAutomata buchi = automata.getBuchiAutomata();
        terminal = buchi.getAcceptSet(acceptSet).contains(node);
        nextAcceptSet = (terminal) ? (acceptSet + 1) % buchi.getAcceptSetsCount()
                                   : acceptSet;

        resetIterator();
    }

    public IState getState() {
        return state;
    }

    public IBuchiNode getNode() {
        return node;
    }

    public int getAcceptSet() {
        return acceptSet;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public Collection<IntersectionTransition> getOutcomingTransitions() {
        return Collections.unmodifiableCollection(transitions);
    }

    protected void addTransition(IntersectionTransition transition) {
        transitions.add(transition);
    }

    public IntersectionNode next() {
        return (iterator.hasNext()) ? iterator.next() : null;
    }

    public void resetIterator() {
        iterator = new NodeIterator();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntersectionNode)) {
            return false;
        }

        IntersectionNode intersectionNode = (IntersectionNode) o;

        return node.equals(intersectionNode.node) && state.equals(intersectionNode.state)
                && (acceptSet == intersectionNode.acceptSet);
    }

    public int hashCode() {
        int result;
        result = state.hashCode();
        result = 31 * result + node.hashCode();
        return result;
    }

    public String toString() {
        return String.format("[\"%s\", %d, %d]", state.getName(), node.getID(), acceptSet);
    }

    private class NodeIterator implements Iterator<IntersectionNode> {

        private Iterator<IStateTransition> stateIter;
        private Iterator<Map.Entry<ITransitionCondition, IBuchiNode>> nodeIter;

        private IStateTransition nextStateTransition;
        private IntersectionNode next;

        private NodeIterator() {
            stateIter = state.getOutcomingTransitions().iterator();
            nodeIter = node.getTransitions().entrySet().iterator();
            if (stateIter.hasNext()) {
                nextStateTransition = stateIter.next();
            }
        }

        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            Map.Entry<ITransitionCondition, IBuchiNode> nextBuchiTransition;

            automata.getPredicates().setAutomataState(state, nextStateTransition);
            while (true) {
                if (nodeIter.hasNext()) {
                    nextBuchiTransition = nodeIter.next();
                    if (nextBuchiTransition.getKey().getValue()) {
                        next = automata.getNode(nextStateTransition.getTarget(),
                                                nextBuchiTransition.getValue(), nextAcceptSet);
                        return true;
                    }
                } else if (stateIter.hasNext()) {
                    nextStateTransition = stateIter.next();
                    nodeIter = node.getTransitions().entrySet().iterator();
                    automata.getPredicates().setAutomataState(state, nextStateTransition);
                } else {
                    next = null;
                    return false;
                }
            }
        }

        public IntersectionNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            IntersectionNode res = next;
            next = null;
            return res;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}