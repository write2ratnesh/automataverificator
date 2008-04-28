/**
 * ITree.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface ITree<S extends IState> {

    ITreeNode<S> getRoot();
}
