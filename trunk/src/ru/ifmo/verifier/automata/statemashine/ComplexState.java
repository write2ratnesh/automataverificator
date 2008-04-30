/**
 * ComplexState.java, 26.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;
import ru.ifmo.verifier.automata.tree.StateTree;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexState implements IState {

    private IComplexStateFactory<ComplexState> factory;

    private String name;
    private ITree<IState> tree;
    private Set<IStateTransition> transitions;

    private IState activeState;

    public ComplexState(ITree<IState> tree, IComplexStateFactory<ComplexState> factory) {
        if (tree == null) {
            throw new IllegalArgumentException("tree can't be null");
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
            buf.insert(buf.length() - 2, '>');
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
            transitions = new HashSet<IStateTransition>();
            addIfActive(tree.getRoot());
        }
        return transitions;
    }

    private void addIfActive(ITreeNode<IState> node) {
        if (node.isActive()) {
            for (IStateTransition trans: node.getState().getOutcomingTransitions()) {
                transitions.add(createTransition(node, trans));
            }
            for (ITreeNode<IState> child: node.getChildren()) {
                addIfActive(child);
            }
        }
    }

    private IStateTransition createTransition(ITreeNode<IState> node, IStateTransition trans) {
        ITree<IState> nextStateTree = new StateTree(tree, node, trans);
        ComplexState nextState = factory.getState(nextStateTree);
        
        return new ComplexTransition(trans, nextState);
    }

    private void nameBuild(ITreeNode<IState> node, StringBuilder buf) {
        buf.append('\"').append(node.getState().getName()).append("\", ");
        for (ITreeNode<IState> n: node.getChildren()) {
            nameBuild(n, buf);
        }
    }

    private void checkActiveState() throws IllegalStateException {
        if (activeState == null) {
            throw new IllegalComplexStateException("Active state is null. Set it to get state actions.");
        }
    }
}
