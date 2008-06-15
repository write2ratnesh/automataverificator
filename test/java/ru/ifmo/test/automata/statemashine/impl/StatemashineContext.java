/**
 * StatemashineContext.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.impl;

import com.evelopers.unimod.runtime.context.StateMachineContext;
import com.evelopers.unimod.runtime.EventProvider;
import com.evelopers.unimod.runtime.ControlledObject;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class StatemashineContext {

    Class<? extends EventProvider> eProvider;
    Class<? extends ControlledObject> cObject;

    List<String> events;
    List<String> actions;

    State init;
    List<State> states;

    public StatemashineContext(Class<? extends EventProvider> eProvider,
                               Class<? extends ControlledObject> cObject) {
        this.eProvider = eProvider;
        this.cObject = cObject;
        events = findEvents(eProvider);
        if (events.size() < 2) {
            throw new IllegalArgumentException("Should be more than one event in event provider");
        }
        actions = findMethods(cObject);
        if (actions.size() < 2) {
            throw new IllegalArgumentException("Should be more than one out action in controlled object");
        }

        states = new ArrayList<State>();
        init = new State("s0");
    }

    public Class<? extends EventProvider> getEventProviderClass() {
        return eProvider;
    }

    public Class<? extends ControlledObject> getControlledObjectClass() {
        return cObject;
    }

    public State getInitState() {
        return init;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<String> getActions() {
        return actions;
    }

    public List<State> getStates() {
        return Collections.unmodifiableList(states);
    }

    public State generateState() {
        State s = new State ("s" + (states.size() + 1));
        states.add(s);
        return s;
    }


    protected List<String> findEvents(Class clazz) {
        List<String> events = new ArrayList<String>();
        for (Field f: clazz.getFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    Object o = f.get(null);
                    if (o instanceof String) {
                        String name = (String) o;
                        events.add(name);
                    }
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
        return events;
    }

    protected List<String> findMethods(Class clazz) {
        List<String> actions = new ArrayList<String>();
        for (Method m: clazz.getMethods()) {
            Class[] params = m.getParameterTypes();
            if (params.length == 1 && StateMachineContext.class.isAssignableFrom(params[0])
                    && m.getReturnType().equals(void.class)) {
                actions.add(m.getName());
            }
        }
        return actions;
    }
}
