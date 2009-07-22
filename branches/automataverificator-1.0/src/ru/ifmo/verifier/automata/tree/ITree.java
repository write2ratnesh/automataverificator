/**
 * ITree.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface ITree<S extends IState> {

    ITreeNode<S> getRoot();

    ITreeNode<S> getNodeForStateMashine(IStateMashine<S> stateMashine);
}
