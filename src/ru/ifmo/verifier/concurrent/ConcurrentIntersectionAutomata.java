/*
 * Developed by eVelopers Corporation - 08.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.verifier.automata.IIntersectionAutomata;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentIntersectionAutomata<S extends IState> implements IIntersectionAutomata<S> {
    /**
     * The default load factor for nodeMap.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int threadNumber;
    private IPredicateFactory<S> predicates;
    private IBuchiAutomata buchiAutomata;

    private Map<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>> nodeMap;
    private Map<S, ConcurrentMap<IBuchiNode, Lock>> lockMap;
    private Collection<? extends Thread> threads;

    public ConcurrentIntersectionAutomata(IPredicateFactory<S> predicates, IBuchiAutomata buchi, int initialCapacity, int threadNumber) {
        if (buchi == null || predicates == null || initialCapacity < 0 || threadNumber <= 0) {
            throw new IllegalArgumentException();
        }
        this.predicates = predicates;
        this.buchiAutomata = buchi;
        this.threadNumber = threadNumber;

        nodeMap = new ConcurrentHashMap<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>>(
                initialCapacity, DEFAULT_LOAD_FACTOR, threadNumber);
        lockMap = new ConcurrentHashMap<S, ConcurrentMap<IBuchiNode, Lock>>(initialCapacity, DEFAULT_LOAD_FACTOR, threadNumber);
    }

    public void setThreads(Collection<? extends Thread> threads) {
        if (threads == null || threads.size() != threadNumber) {
            throw new IllegalArgumentException();
        }
        this.threads = threads;
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
                            buchiAutomata.size() * buchiAutomata.getAcceptSetsCount(),
                            DEFAULT_LOAD_FACTOR, threadNumber);
                    nodeMap.put(state, buchiMap);
                }
            }
        }

        Map<Integer, IntersectionNode<S>> acceptMap = buchiMap.get(node);
        IntersectionNode<S> res;
        if (acceptMap == null) {
            Lock lock = getLock(state, node);
            lock.lock();
            try {
                acceptMap = buchiMap.get(node);
                if (acceptMap == null) {
                    acceptMap = new ConcurrentHashMap<Integer, IntersectionNode<S>>(
                            buchiAutomata.getAcceptSetsCount(), DEFAULT_LOAD_FACTOR, threadNumber);
                    buchiMap.put(node, acceptMap);

                    res = new IntersectionNode<S>(this, state, node, acceptSet, threads);
                    acceptMap.put(acceptSet, res);
                    return res;
                }
            } finally {
                lock.unlock();
            }
        }

        res = acceptMap.get(acceptSet);
        if (res == null) {
            Lock lock = getLock(state, node);
            lock.lock();
            try {
                res = acceptMap.get(acceptSet);
                if (res == null) {
                    res = new IntersectionNode<S>(this, state, node, acceptSet, threads);
                    acceptMap.put(acceptSet, res);
                }
            } finally {
                lock.unlock();
            }
        }
        return res;
    }

    protected Lock getLock(S state, IBuchiNode node) {
        ConcurrentMap<IBuchiNode, Lock> buchiMap = lockMap.get(state);
        if (buchiMap == null) {
            synchronized (state) {
                buchiMap = lockMap.get(state);
                if (buchiMap == null) {
                    buchiMap = new ConcurrentHashMap<IBuchiNode, Lock>(buchiAutomata.size(),
                            DEFAULT_LOAD_FACTOR, threadNumber);
                    lockMap.put(state, buchiMap);

                    buchiMap.put(node, new ReentrantLock());
                }
            }
        }

        Lock lock = buchiMap.get(node);
        if (lock == null) {
            buchiMap.putIfAbsent(node, new ReentrantLock());
        }

        return buchiMap.get(node);
    }

    public IPredicateFactory<S> getPredicates() {
        return predicates;
    }
}
