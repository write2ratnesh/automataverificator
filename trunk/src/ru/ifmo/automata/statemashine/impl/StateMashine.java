/**
 * StateMashine.java, 02.03.2008
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The IStateMashine implementation
 *
 * @author: Kirill Egorov
 */
public class StateMashine<S extends IState> implements IStateMashine<S> {
    protected static final String METHOD_PATTERN = "\\p{Alpha}\\w*\\.\\p{Alpha}\\w*";

    private String name;
    private S initialState;
    private Map<String, S> states = new HashMap<String, S>();
    private Set<IEventProvider> eventProviders = new HashSet<IEventProvider>();
    private Map<String, IControlledObject> ctrlObjects = new HashMap<String, IControlledObject>();

    private Map<String, Map<String, ?>> sources;
    private Map<S, List<IFunction>> functions;

    private IStateMashine<S> parentStateMashine;
    private S parentState;

    private Set<IStateMashine<S>> nestedStateMashines = new LinkedHashSet<IStateMashine<S>>();

    public StateMashine(String name) {
        this.name = name;
    }

    protected void createSource() {
        sources = new HashMap<String, Map<String, ?>>();
        for (Map.Entry<String, IControlledObject> e: ctrlObjects.entrySet()) {
            Map<String, Object> functions = new HashMap<String, Object>();
            for (IFunction f: e.getValue().getFunctions()) {
                functions.put(f.getName(), f.getCurValue());
            }
            sources.put(e.getKey(), functions);
        }
    }

    public String getName() {
        return name;
    }

    public IStateMashine<S> getParentStateMashine() {
        return parentStateMashine;
    }

    public Set<IStateMashine<S>> getNestedStateMashines() {
        return nestedStateMashines;
    }

    public S getParentState() {
        return parentState;
    }

    public <T extends IStateMashine<S>> void setParent(T parentStateMashine, S parentState) {
        if (parentStateMashine == null || parentState == null) {
            throw new IllegalArgumentException("parent parameters can't be null");
        }
        if (isNested()) {
            throw new UnsupportedOperationException("State mashine can't have more than one parent");
        }
//        if (!parentState.equals(parentStateMashine.getState(parentState.getName()))) {
//            throw new IllegalArgumentException("parentState isn't parentStateMashine state");
//        }
        if (!parentState.getNestedStateMashines().contains(this)) {
            throw new IllegalArgumentException("This stateMashine isn't nested stateMashine for parentState");
        }
        this.parentStateMashine = parentStateMashine;
        this.parentState = parentState;
    }

    public void addNestedStateMashine(IStateMashine<S> stateMashine) {
        nestedStateMashines.add(stateMashine);
    }

    public boolean isNested() {
        return parentStateMashine != null;
    }

    public S getInitialState() {
        if (initialState == null) {
            throw new RuntimeException("Automamta hasnot been initialized yet or has not initial state");
        }
        return initialState;
    }

    public S getState(String stateName) {
        return states.get(stateName);
    }

    public Collection<S> getStates() {
        return states.values();
    }

    public Set<IEventProvider> getEventProviders() {
        return eventProviders;
    }

    public void addEventProvider(IEventProvider provider) {
        eventProviders.add(provider);
    }

    public IControlledObject getControlledObject(String association) {
        return ctrlObjects.get(association);
    }

    public Collection<IControlledObject> getControlledObjects() {
        return ctrlObjects.values();
    }

    public Map<String, Map<String, ?>> getSources() {
        if (sources == null) {
            createSource();
        }
        return sources;
    }

    public void addState(S s) {
        checkInitial(s);
        states.put(s.getName(), s);
    }

    public void addStates(Map<String, S> states) {
        for (Map.Entry<String, S> entry: states.entrySet()) {
            this.states.put(entry.getKey(), entry.getValue());
            checkInitial(entry.getValue());
        }
    }

    private void checkInitial(S s)  {
        if (StateType.INITIAL == s.getType()) {
            if (initialState != null) {
                throw new IllegalArgumentException("StateMashine can't contain more than one initial state");
            }
            initialState = s;
        }
    }

    public void addControlledObject(String association, IControlledObject ctrlObject) {
        ctrlObjects.put(association, ctrlObject);
    }

    public List<IFunction> getStateFunctions(S state) {
        if (!states.containsValue(state)) {
            throw new IllegalArgumentException("State mashine doesn't contain this state: " + state);
        }
        synchronized (this) {
            if (functions == null) {
                functions = new HashMap<S, List<IFunction>>();
            }
            List<IFunction> stateFns = functions.get(state);
            if (stateFns == null) {
                stateFns = createStateFunctions(state);
                functions.put(state, stateFns);
            }
            return Collections.unmodifiableList(stateFns);
        }
    }

    private List<IFunction> createStateFunctions(IState state) {
        List<IFunction> list = new ArrayList<IFunction>();
        for (String fName: getInvocations(state)) {
            int i = fName.indexOf('.');
            String ctrlObj = fName.substring(0, i);
            String funName = fName.substring(i + 1);
            IFunction fun = ctrlObjects.get(ctrlObj).getFunction(funName);
            list.add(fun);
        }
        return list;
    }

    private Set<String> getInvocations(IState state) {
        Set<String> invocations = new HashSet<String>();
        for (IStateTransition t: state.getOutcomingTransitions()) {
            if (t.getCondition() != null) {
                invocations.addAll(getInvocations(t.getCondition().getExpression()));
            }
        }
        return invocations;
    }

    private Set<String> getInvocations(String expr) {
        if (expr == null) {
            return Collections.emptySet();
        }
        Set<String> invocations = new HashSet<String>();
        Pattern p = Pattern.compile(METHOD_PATTERN);
        Matcher m = p.matcher(expr);
        while (m.find()) {
            invocations.add(expr.substring(m.start(), m.end()));
        }
        return invocations;
    }

    public String toString() {
        return name; 
    }
}
