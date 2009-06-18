/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;

import java.util.*;

/**
 * IState implementation whithout nested state mashines
 * @author kegorov
 *         Date: Jun 18, 2009
 */
public class SimpleState implements IState {
    private String name;
    private StateType type;
    private List<IAction> actions;
    private Collection<IStateTransition> outTransitions =
            new ArrayList<IStateTransition>();

    public SimpleState(String name, StateType type, List<IAction> actions) {
        this.name = name;
        this.type = type;
        this.actions = actions;

        outTransitions.add(new Transition(null, null, this));
    }

    public String getName() {
        return name;
    }

    public StateType getType() {
        return type;
    }

    public List<IAction> getActions() {
        return actions;
    }

    public boolean isTerminal() {
        return type == StateType.FINAL;
    }

    public Collection<IStateTransition> getOutcomingTransitions() {
        return outTransitions;
    }

    public Set<IStateMashine<? extends IState>> getNestedStateMashines() {
        throw new UnsupportedOperationException();
    }

    public String getUniqueName() {
        return name + '@' + Integer.toHexString(super.hashCode());
    }

    public void addOutcomingTransition(IStateTransition t) {
        outTransitions.add(t);
    }

    public void addNestedStateMashine(IStateMashine<? extends IState> m) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return name;
    }
}
