/**
 * StateTree.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.ITransition;

import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class StateTree<S extends IState> implements ITree<S> {

    private ITreeNode<S> root;

    protected StateTree(ITreeNode<S> root) {
        this.root = root;
    }

    public ITreeNode<S> getRoot() {
        return root;
    }
}
