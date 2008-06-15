/**
 * Transition.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.impl;

import java.util.List;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class Transition {
    private String event;
    private List<String> actions;
    private State target;

    public Transition(String event, List<String> actions, State target) {
        this.event = event;
        this.actions = actions;
        this.target = target;
    }

    public String getEvent() {
        return event;
    }

    public List<String> getActions() {
        return actions;
    }

    public State getTarget() {
        return target;
    }
}
