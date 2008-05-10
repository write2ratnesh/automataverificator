/*
 * Developed by eVelopers Corporation - 08.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.LtlParseException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiThreadVerifier<S extends IState> implements IVerifier<S> {
    private int threadNumber;
    private int stateCount;
    private S initState;
    private ILtlParser parser;
    private ITranslator translator = new SimpleTranslator();

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads equals available processors for VM
     * @param initState automata initial state
     * @param stateCount state mashine states count
     */
    public MultiThreadVerifier(S initState, int stateCount) {
        this(initState, stateCount, 0);

    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>
     * @param initState automata initial state
     * @param stateCount state mashine states count
     * @param threadNumber number of threads
     */
    public MultiThreadVerifier(S initState, int stateCount, int threadNumber) {
        this(initState, null, stateCount, threadNumber);

    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>.
     * @param initState automata initial state
     * @param parser ltl formula parser instance
     * @param stateCount state mashine states count
     */
    public MultiThreadVerifier(S initState, ILtlParser parser, int stateCount) {
        this(initState, parser, new SimpleTranslator(), stateCount, 0);
    }

    public MultiThreadVerifier(S initState, ILtlParser parser, int stateCount, int threadNumber) {
        this(initState, parser, new SimpleTranslator(), stateCount, threadNumber);
    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>
     * @param initState automata initial state
     * @param parser ltl formula parser
     * @param translator ltl to buchi translator
     * @param stateCount state mashine states count
     * @param threadNumber number of threads
     */
    public MultiThreadVerifier(S initState, ILtlParser parser, ITranslator translator, int stateCount, int threadNumber) {
        if (initState == null || translator == null || stateCount <= 0) {
            throw new IllegalArgumentException();
        }

        this.initState = initState;
        this.parser = parser;
        this.translator = translator;
        this.stateCount = stateCount;

        if (threadNumber > 0) {
            this.threadNumber = threadNumber;
        } else {
            this.threadNumber = Runtime.getRuntime().availableProcessors();
        }
    }

    /**
     * Get number of threads
     * @return number of threads
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    public void setParser(ILtlParser parser) {
        this.parser = parser;
    }

    public List<IInterNode> verify(String ltlFormula, IPredicateFactory<S> predicates) throws LtlParseException {
        if (parser == null) {
            throw new UnsupportedOperationException("Can't verify LTL formula without LTL parser."
                    + "Define it first or use List<IStateTransition> verify(IBuchiAutomata buchi) method instead");
        }
        LtlNode ltl = parser.parse(ltlFormula);
        ltl = LtlUtils.getInstance().neg(ltl);
        IBuchiAutomata buchi = translator.translate(ltl);

        //TODO: -----------------------
        System.out.println("LTL: " + ltlFormula);
        System.out.println(buchi);
        //-----------------------------

        return verify(buchi, predicates);
    }

    public List<IInterNode> verify(IBuchiAutomata buchi, IPredicateFactory<S> predicates) {
        if (!(predicates instanceof MultiThreadPredicateFactory)) {
            throw new IllegalArgumentException("Unexpected predicates class: "
                    + predicates.getClass() + ". Expected instance of "
                    + MultiThreadPredicateFactory.class);
        }
        int initialCapacity = stateCount * buchi.size();
        ConcurrentIntersectionAutomata<S> automata = new ConcurrentIntersectionAutomata<S>(
                predicates, buchi, initialCapacity, threadNumber);
        Set<IntersectionNode> visited = new ConcurrentHashSet<IntersectionNode>(initialCapacity);

        //create threadNumber threads and start them
        List<DfsThread> threads = new ArrayList<DfsThread>(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threads.add(new DfsThread(null, visited));
        }
        automata.setThreads(threads);
        ((MultiThreadPredicateFactory) predicates).init(threads);

        IntersectionNode initial = automata.getNode(initState, buchi.getStartNode(), 0);

        for (DfsThread t: threads) {
            t.setInitial(initial);
            t.start();
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //check results
        Deque<IntersectionNode> stack = new LinkedList<IntersectionNode>();
        for (DfsThread t: threads) {
            if (!t.getResult().isEmpty()) {
                stack = t.getResult();
                break;
            }
        }

        List<IInterNode> res = new ArrayList<IInterNode>(stack.size());

        for (Iterator<? extends IInterNode> iter = stack.descendingIterator(); iter.hasNext();) {
            res.add(iter.next());
        }
        return res;
    }

    private class ConcurrentHashSet<E> extends AbstractSet<E> {
        ConcurrentHashMap<E, Object> map;

        private ConcurrentHashSet(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException();
            }
            map = new ConcurrentHashMap<E, Object>(initialCapacity, threadNumber);
        }

        public int size() {
            return map.size();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean contains(Object o) {
            return map.containsKey(o);
        }

        public Iterator<E> iterator() {
            return map.keySet().iterator();
        }

        public boolean add(E e) {
            return map.putIfAbsent(e, Boolean.TRUE) == null;
        }

        public boolean remove(Object o) {
            return map.remove(o, Boolean.TRUE);
        }

        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }
    }
}
