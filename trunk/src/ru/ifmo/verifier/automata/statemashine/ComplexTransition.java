/**
 * ComplexTransition.java, 28.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.*;

import java.util.List;

/**
 * The transition from ComplexState to ComplexState
 *
 * @author: Kirill Egorov
 */
public class ComplexTransition implements IStateTransition {
    private IStateTransition transition;
    private ComplexState target;

    public ComplexTransition(IStateTransition transition, ComplexState target) {
        this.transition = transition;
        this.target = target;
    }

    public IState getTarget() {
        target.setActiveState(transition.getTarget());
        return target;
    }

    public IEvent getEvent() {
        return transition.getEvent();
    }

    public List<IAction> getActions() {
        return transition.getActions();
    }

    public ICondition getCondition() {
        return transition.getCondition();
    }
}
