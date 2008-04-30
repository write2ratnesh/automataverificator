/**
 * StateTree.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.tree;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.ITransition;

import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class StateTree implements ITree<IState> {

    private ITreeNode<IState> root;

    public StateTree(ITreeNode<IState> root) {
        this.root = root;
    }

    public StateTree(ITree<IState> tree, ITreeNode<IState> node, IStateTransition trans) {
        assert root.isActive();
        root = copy(root, node, trans);
    }

    public ITreeNode<IState> getRoot() {
        return root;
    }

    protected ITreeNode<IState> copy(ITreeNode<IState> node, ITreeNode<IState> fromNode, IStateTransition trans) {
        boolean isTransNode = (node == fromNode);
        IState state = isTransNode ? trans.getTarget() : node.getState();
        TreeNode<IState> newNode = new TreeNode<IState>(state, node.getStateMashine(), node.isActive());

        if (isTransNode) {
            assert node.isActive();
            
            for (ITreeNode<IState> child: node.getChildren()) {
                newNode.addChildren(copyTransSubnode(child));
            }
        } else {
            for (ITreeNode<IState> child: node.getChildren()) {
                newNode.addChildren(copy(child, fromNode, trans));
            }
        }
        return newNode;
    }

    protected ITreeNode<IState> copyTransSubnode(ITreeNode<IState> node) {
        TreeNode<IState> newNode = new TreeNode<IState>(node.getState(), node.getStateMashine(), false);

        for (ITreeNode<IState> child: node.getChildren()) {
            newNode.addChildren(copyTransSubnode(child));
        }
        return newNode;
    }
}
