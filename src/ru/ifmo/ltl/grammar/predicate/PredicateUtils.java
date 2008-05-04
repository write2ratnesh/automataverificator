/**
 * PredicateUtils.java, 12.03.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.ICondition;
import ru.ifmo.automata.statemashine.IEvent;
import org.apache.commons.lang.NotImplementedException;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class PredicateUtils<S extends IState> implements IPredicateUtils<S> {

    protected S state;
    protected IStateTransition transition;

    public void setAutomataState(S state, IStateTransition transition) {
        this.state = state;
        this.transition = transition;
    }

    @Predicate
    public Boolean wasEvent(IEvent e) {
        if (transition.getEvent() == null && transition.getCondition() == null
                && transition.getTarget() == state) {
            return null;
        }
        return e.equals(transition.getEvent());
    }

    @Predicate
    public boolean isInState(IStateMashine<? extends IState> a, IState s) {
        return transition.getTarget().equals(s);
    }

    @Predicate
    public boolean wasInState(IStateMashine<? extends IState> a, IState s) {
        return state.equals(s);
    }

    @Predicate
    public boolean cameToFinalState() {
        return transition.getTarget().isTerminal();
    }

    @Predicate
    public Boolean wasAction(IAction z) {
        if (transition.getEvent() == null && transition.getCondition() == null
                && transition.getTarget() == state) {
            return null;
        }
        return transition.getActions().contains(z) || transition.getTarget().getActions().contains(z);
    }

    @Predicate
    public boolean wasFirstAction(IAction z) {
        if (transition.getActions().isEmpty()) {
            return false;
        } else {
            return transition.getActions().get(0).equals(z);
        }
    }

    @Predicate
    public boolean wasTrue(ICondition cond) {
        throw new NotImplementedException();
    }

    @Predicate
    public boolean wasFalse(ICondition cond) {
        throw new NotImplementedException();
    }
}
