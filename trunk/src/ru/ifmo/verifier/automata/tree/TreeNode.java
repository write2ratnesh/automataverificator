/**
 * TreeNode.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;

import java.util.Set;
import java.util.HashSet;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class TreeNode<S extends IState> implements ITreeNode<S> {
    private S state;
    private IStateMashine<S> stateMashine;
    private boolean active;

    private Set<ITreeNode<S>> children;

    public TreeNode(S state, IStateMashine<S> stateMashine, boolean active) {
        this.state = state;
        this.stateMashine = stateMashine;
        this.active = active;
    }

    public S getState() {
        return state;
    }

    public IStateMashine<S> getStateMashine() {
        return stateMashine;
    }

    public boolean isActive() {
        return active;
    }

    public synchronized void addChildren(ITreeNode<S> node) {
        if (children == null) {
            children = new HashSet<ITreeNode<S>>();
        }
        children.add(node);
    }

    /**
     * Get modifiable set of children
     * @return set of children nodes
     */
    public Set<ITreeNode<S>> getChildren() {
        return children;
    }
}
