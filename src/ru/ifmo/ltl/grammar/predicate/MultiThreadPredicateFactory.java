/**
 * MultiThreadPredicateFactory.java, 10.05.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class MultiThreadPredicateFactory<S extends IState> implements IPredicateFactory<S> {
    private AbstractPredicateFactory<S> predicates;
    ConcurrentMap<Thread, IPredicateFactory<S>> predicateMap = new ConcurrentHashMap<Thread, IPredicateFactory<S>>();

    public MultiThreadPredicateFactory(AbstractPredicateFactory<S> predicates) {
        this.predicates = predicates;
    }

    protected IPredicateFactory<S> getPredicate() {
        Thread t = Thread.currentThread();
        IPredicateFactory<S> p = predicateMap.get(t);
        if (p == null) {
            try {
                p = predicates.clone();
                predicateMap.put(t, p);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public void setAutomataState(S state, IStateTransition transition) {
        getPredicate().setAutomataState(state, transition);
    }

    @Predicate
    public Boolean wasEvent(IEvent e) {
        return getPredicate().wasEvent(e);
    }

    @Predicate
    public boolean isInState(IStateMashine<? extends IState> a, IState s) {
        return getPredicate().isInState(a, s);
    }

    @Predicate
    public boolean wasInState(IStateMashine<? extends IState> a, IState s) {
        return getPredicate().wasInState(a, s);
    }

    @Predicate
    public boolean cameToFinalState() {
        return getPredicate().cameToFinalState();
    }

    @Predicate
    public Boolean wasAction(IAction z) {
        return getPredicate().wasAction(z);
    }

    @Predicate
    public boolean wasFirstAction(IAction z) {
        return getPredicate().wasFirstAction(z);
    }

    @Predicate
    public boolean wasTrue(ICondition cond) {
        return getPredicate().wasTrue(cond);
    }

    @Predicate
    public boolean wasFalse(ICondition cond) {
        return getPredicate().wasFalse(cond);
    }
}
