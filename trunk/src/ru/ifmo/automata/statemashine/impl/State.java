/**
 * State.java, 02.03.2008
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.StateType;

import java.util.*;

/**
 * IState implementation
 *
 * @author Kirill Egorov
 */
public class State implements IState {
    private String name;
    private StateType type;
    private List<IAction> actions;
    private Collection<IStateTransition> outTransitions =
            new ArrayList<IStateTransition>();
    private Set<IStateMashine<? extends IState>> nestedStateMashines = new LinkedHashSet<IStateMashine<? extends IState>>();

    public State(String name, StateType type, List<IAction> actions) {
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
        return nestedStateMashines;
    }

    public String getUniqueName() {
        return name + '@' + Integer.toHexString(super.hashCode());
    }

    public void addOutcomingTransition(IStateTransition t) {
        outTransitions.add(t);
    }

    public void addNestedStateMashine(IStateMashine<? extends IState> m) {
        nestedStateMashines.add(m);
    }

    public String toString() {
        return name;
    }
}
