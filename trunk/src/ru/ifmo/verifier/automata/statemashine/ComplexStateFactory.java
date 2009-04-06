/**
 * ComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;
import ru.ifmo.verifier.automata.tree.TreeNode;
import ru.ifmo.verifier.automata.tree.StateTree;

import java.util.Map;
import java.util.HashMap;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class ComplexStateFactory<S extends IState> implements IComplexStateFactory<S> {

    private Map<ITree<S>, ComplexState<S>> stateMap = new HashMap<ITree<S>, ComplexState<S>>();
//    private ComplexState initial;
    
    private ComplexStateFactory() {
        //
    }

    public static <S extends IState> ComplexState createInitialState(IStateMashine<S> stateMashine) {
        ComplexStateFactory<S> factory = new ComplexStateFactory<S>();
        ITree<S> initTree = new StateTree<S>(factory.getInitial(stateMashine, true));

        return new ComplexState<S>(initTree, factory);
    }


    /*public ComplexState getInitialState(IStateMashine<IState> stateMashine) {
        if (initial == null) {
            ITree<IState> initTree = new StateTree(getInitial(stateMashine, true));
            
            initial = new ComplexState(initTree, this);
        }
        return initial;
    }*/

    public ComplexState<S> getState(ITree<S> tree) {
        ComplexState<S> res = stateMap.get(tree);
        if (res == null) {
            res = new ComplexState<S>(tree, this);
            stateMap.put(tree, res);
        }
        return res;
    }

    private ITreeNode<S> getInitial(IStateMashine<S> stateMashine, boolean active) {
        TreeNode<S> node = new TreeNode<S>(stateMashine.getInitialState(), stateMashine, active);

        for (IStateMashine<S> sm: stateMashine.getNestedStateMashines()) {
            //Only root node can be active
            node.addChildren(getInitial(sm, false));
        }
        return node;
    }
}
