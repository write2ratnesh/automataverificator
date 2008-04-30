/**
 * ComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;
import ru.ifmo.verifier.automata.tree.TreeNode;
import ru.ifmo.verifier.automata.tree.StateTree;
import org.apache.commons.lang.NotImplementedException;

import java.util.Map;
import java.util.HashMap;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexStateFactory implements IComplexStateFactory<ComplexState> {

    private Map<ITree<IState>, ComplexState> stateMap = new HashMap<ITree<IState>, ComplexState>();
    private ComplexState initial;

    public ComplexState getInitialState(IStateMashine<IState> stateMashine) {
        if (initial == null) {
            ITree<IState> initTree = new StateTree(getInitial(stateMashine, true));
            
            initial = new ComplexState(initTree, this);
        }
        return initial;
    }

    public ComplexState getState(ITree<IState> tree) {
        ComplexState res = stateMap.get(tree);
        if (res == null) {
            res = new ComplexState(tree, this);
            stateMap.put(tree, res);
        }
        return res;
    }

    private ITreeNode<IState> getInitial(IStateMashine<IState> stateMashine, boolean active) {
        TreeNode<IState> node = new TreeNode<IState>(stateMashine.getInitialState(), stateMashine, active);

        for (IStateMashine<IState> sm: stateMashine.getNestedStateMashines()) {
            //Only root node can be active
            node.addChildren(getInitial(sm, false));
        }
        return node;
    }
}
