/**
 * ComplexState.java, 26.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexState implements IState {

    private ITree<IState> tree;
    private Set<IStateTransition> transitions;

    public ComplexState() {

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
                transitions.add(createTransition(trans));
            }
            for (ITreeNode<IState> child: node.getChildren()) {
                addIfActive(child);
            }
        }
    }

    private IStateTransition createTransition(IStateTransition trans) {
        
    }
}
