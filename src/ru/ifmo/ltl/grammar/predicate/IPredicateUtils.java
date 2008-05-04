/**
 * IPredicateUtils.java, 12.04.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;

/**
 * Predicate provider.
 * User @Predicate annotation, to mark method as predicate;
 *
 * @author: Kirill Egorov
 */
public interface IPredicateUtils<S extends IState> {

    /**
     * To check predicate in transition.getTarget() state.
     * @param state previous state
     * @param transition transition from state to transition.getTarget()
     */
    void setAutomataState(S state, IStateTransition transition);
}
