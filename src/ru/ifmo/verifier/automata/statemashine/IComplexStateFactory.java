/**
 * IComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.verifier.automata.tree.ITree;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface IComplexStateFactory<S extends IState> {

//    S getInitialState(IStateMachine<IState> stateMachine);

    ComplexState<S> getState(ITree<S> tree);
}
