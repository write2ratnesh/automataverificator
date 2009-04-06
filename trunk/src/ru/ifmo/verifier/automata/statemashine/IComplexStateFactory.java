/**
 * IComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.verifier.automata.tree.ITree;
import ru.ifmo.verifier.automata.tree.ITreeNode;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface IComplexStateFactory<S extends IState> {

//    S getInitialState(IStateMashine<IState> stateMashine);

    ComplexState<S> getState(ITree<S> tree);
}
