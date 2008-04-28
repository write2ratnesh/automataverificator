/**
 * IComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.verifier.automata.tree.ITree;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IComplexStateFactory {

    IState getState(ITree<IState> tree);

    IState addState(ComplexState cs, IStateTransition trans);
}
