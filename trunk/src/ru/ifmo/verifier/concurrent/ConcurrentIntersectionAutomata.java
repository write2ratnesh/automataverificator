/*
 * Developed by eVelopers Corporation - 08.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.automata.IIntersectionAutomata;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentIntersectionAutomata<S extends IState> implements IIntersectionAutomata<S> {
    /**
     * The default load factor for nodeMap.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int threadNumber;
    private IPredicateUtils<S> predicates;
    private IBuchiAutomata buchiAutomata;

    private Map<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>> nodeMap;

    public ConcurrentIntersectionAutomata(IPredicateUtils<S> predicates, IBuchiAutomata buchi, int initialCapacity, int threadNumber) {
        if (buchi == null || predicates == null || initialCapacity < 0 || threadNumber <= 0) {
            throw new IllegalArgumentException();
        }
        this.predicates = predicates;
        this.buchiAutomata = buchi;
        this.threadNumber = threadNumber;

        nodeMap = new ConcurrentHashMap<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>>(
                initialCapacity, DEFAULT_LOAD_FACTOR, threadNumber);
    }

    public IBuchiAutomata getBuchiAutomata() {
        return buchiAutomata;
    }

    public IntersectionNode<S> getNode(S state, IBuchiNode node, int acceptSet) {
        Map<IBuchiNode, Map<Integer, IntersectionNode<S>>> buchiMap = nodeMap.get(state);
        if (buchiMap == null) {
            synchronized (state) {
                buchiMap = nodeMap.get(state);
                if (buchiMap == null) {
                    buchiMap = new ConcurrentHashMap<IBuchiNode, Map<Integer, IntersectionNode<S>>>(
                            buchiAutomata.size() * buchiAutomata.getAcceptSetsCount(), DEFAULT_LOAD_FACTOR, threadNumber);
                    nodeMap.put(state, buchiMap);
                }
            }
        }

        Map<Integer, IntersectionNode<S>> acceptMap = buchiMap.get(node);
        if (acceptMap == null) {
            synchronized (node) {
                acceptMap = buchiMap.get(node);
                if (acceptMap == null) {
                    acceptMap = new ConcurrentHashMap<Integer, IntersectionNode<S>>(
                            buchiAutomata.getAcceptSetsCount(), DEFAULT_LOAD_FACTOR, threadNumber);
                    buchiMap.put(node, acceptMap);
                }
            }
        }

        IntersectionNode<S> res = acceptMap.get(acceptSet);
        if (res == null) {
            res = new IntersectionNode<S>(this, state, node, acceptSet);
            acceptMap.put(acceptSet, res);
        }
        return res;
    }

    public IPredicateUtils<S> getPredicates() {
        return predicates;
    }
}
