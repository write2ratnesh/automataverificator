/**
 * IState.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine;

import ru.ifmo.automata.INode;

import java.util.List;
import java.util.Set;

/**
 * The state mashine state
 *
 * @author: Kirill Egorov
 */
public interface IState extends INode<IStateTransition> {

    String getName();

    StateType getType();

    List<IAction> getActions();

    Set<IStateMashine<? extends IState>> getNestedStateMashines();
}