/**
 * IPredicateFactory.java, 10.05.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IPredicateFactory<S extends IState> {

    void setAutomataState(S state, IStateTransition transition);

    @Predicate
    Boolean wasEvent(IEvent e);

    @Predicate
    boolean isInState(IStateMashine<? extends IState> a, IState s);

    @Predicate
    boolean wasInState(IStateMashine<? extends IState> a, IState s);

    @Predicate
    boolean cameToFinalState();

    @Predicate
    Boolean wasAction(IAction z);

    @Predicate
    boolean wasFirstAction(IAction z);

    @Predicate
    boolean wasTrue(ICondition cond);

    @Predicate
    boolean wasFalse(ICondition cond);
}
