/**
 * State.java, 02.03.2008
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.StateType;

import java.util.*;

/**
 * IState implementation. May contain nested state mashine.
 *
 * @author Kirill Egorov
 */
public class State extends SimpleState {
    private Set<IStateMashine<? extends IState>> nestedStateMashines = new LinkedHashSet<IStateMashine<? extends IState>>();

    public State(String name, StateType type, List<IAction> actions) {
        super(name, type, actions);
    }

    public Set<IStateMashine<? extends IState>> getNestedStateMashines() {
        return nestedStateMashines;
    }

    public void addNestedStateMashine(IStateMashine<? extends IState> m) {
        nestedStateMashines.add(m);
    }
}
