/**
 * ComplexState.java, 26.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;
import ru.ifmo.verifier.automata.tree.StateTree;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexState<S extends IState> implements IState {

    private IComplexStateFactory<S> factory;

    private String name;
    private ITree<S> tree;
    private Set<IStateTransition> transitions;

    private IState activeState;

    private ReadWriteLock transLock = new ReentrantReadWriteLock();

    public ComplexState(ITree<S> tree, IComplexStateFactory<S> factory) {
        if (tree == null) {
            throw new IllegalArgumentException("ITree parameter can't be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("IComplexStateFactory parameter can't be null");
        }
        this.tree = tree;
        this.factory = factory;
    }

    public String getName() {
        if (name == null) {
            StringBuilder buf = new StringBuilder("<");

            nameBuild(tree.getRoot(), buf);
            buf.replace(buf.length() - 2, buf.length(), ">");
            name = buf.toString();
        }
        return name;
    }

    public StateType getType() {
        return (activeState != null) ? activeState.getType() : StateType.NORMAL;
    }

    public List<IAction> getActions() throws IllegalStateException {
        checkActiveState();
        return activeState.getActions();
    }

    public Set<IStateMashine<? extends IState>> getNestedStateMashines() {
        return Collections.emptySet();
    }

    public boolean isTerminal() throws IllegalStateException {
        checkActiveState();
        return activeState.isTerminal();
    }

    public void setActiveState(IState activeState) {
        this.activeState = activeState;
    }

    public Collection<IStateTransition> getOutcomingTransitions() {
        if (transitions == null) {
            if (transLock.writeLock().tryLock()) {
                try {
                    if (transitions == null) {
                        transitions = new LinkedHashSet<IStateTransition>();
                        addIfActive(tree.getRoot());
                    }
                } finally {
                    transLock.writeLock().unlock();
                }
            }
        }
        transLock.readLock().lock();
        try {
            return transitions;
        } finally {
            transLock.readLock().unlock();
        }
    }

    public S getStateMashineState(IStateMashine<S> stateMashine) {
        ITreeNode<S> node = tree.getNodeForStateMashine(stateMashine);

        assert node.getStateMashine().equals(stateMashine);
        return node.getState();
    }

    private void addIfActive(ITreeNode<S> node) {
        if (node.isActive()) {
            for (IStateTransition trans: node.getState().getOutcomingTransitions()) {
                transitions.add(createTransition(node, trans));
            }
            for (ITreeNode<S> child: node.getChildren()) {
                assert child.isActive() == child.getStateMashine().getParentState().equals(node.getState())
                        : String.format("Child: %s, stateMahine parent state:%s, parentState: %s",
                                        child, child.getStateMashine().getParentState(), node);
                addIfActive(child);
            }
        }
    }

    private ComplexTransition createTransition(ITreeNode<S> node, IStateTransition trans) {
        ITree<S> nextStateTree = new StateTree<S>(tree, node, trans);
        ComplexState<S> nextState = factory.getState(nextStateTree);
        
        return new ComplexTransition(trans, nextState);
    }

    private void nameBuild(ITreeNode<S> node, StringBuilder buf) {
        buf.append('\"').append(node.getState().getName()).append("\", ");
        for (ITreeNode<S> n: node.getChildren()) {
            nameBuild(n, buf);
        }
    }

    private void checkActiveState() throws IllegalStateException {
        if (activeState == null) {
            throw new IllegalComplexStateException("Active state is null. Set it to get state actions.");
        }
    }

    public String toString() {
        return getName();
    }
}
