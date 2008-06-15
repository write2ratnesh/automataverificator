/**
 * State.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.impl;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class State {

    private String name;
    private List<Transition> transitions = new ArrayList<Transition>();

    public State(String name) {
        this.name = name;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
