/**
 * TreeNode.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class TreeNode<S extends IState> implements ITreeNode<S> {
    private S state;
    private IStateMashine<S> stateMashine;
    private boolean active;

    private Map<IStateMashine<S>, ITreeNode<S>> children
            = new LinkedHashMap<IStateMashine<S>, ITreeNode<S>>();

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

    public void addChildren(ITreeNode<S> node) {
        children.put(node.getStateMashine(), node);
    }

    /**
     * Get modifiable set of children
     * @return set of children nodes
     */
    public Collection<ITreeNode<S>> getChildren() {
        return children.values();
    }

    public ITreeNode<S> getChild(IStateMashine<S> stateMashine) {
        return children.get(stateMashine);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;

        TreeNode treeNode = (TreeNode) o;

        if (state != null ? !state.equals(treeNode.state) : treeNode.state != null) return false;

        return true;
    }

    public String toString() {
        return stateMashine.getName() + '.' + state.getName();
    }
}
