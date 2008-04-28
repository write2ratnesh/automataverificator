/**
 * ITreeNode.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.automata.statemashine.IState;

import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface ITreeNode<S extends IState> {
    S getState();

    IStateMashine<S> getStateMashine();

    boolean isActive();

    /**
     * Get modifiable set of children
     * @return set of children nodes
     */
    Set<ITreeNode<S>> getChildren();
}
