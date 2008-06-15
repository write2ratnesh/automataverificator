/*
 * Developed by eVelopers Corporation - 08.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.translator.Ltl2baTranslator;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.util.concurrent.ConcurrentHashSet;
import ru.ifmo.util.concurrent.DfsStackTreeNode;
import ru.ifmo.util.concurrent.DfsStackTree;

import java.util.*;

public class MultiThreadVerifier<S extends IState> implements IVerifier<S> {
    private int threadNumber;
    private int stateCount;
    private S initState;
    private ILtlParser parser;
    private ITranslator translator = new Ltl2baTranslator();

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
        this(initState, parser, new Ltl2baTranslator(), stateCount, 0);
    }

    public MultiThreadVerifier(S initState, ILtlParser parser, ITranslator translator, int stateCount) {
        this(initState, parser, translator, stateCount, 0);
    }

    public MultiThreadVerifier(S initState, ILtlParser parser, int stateCount, int threadNumber) {
        this(initState, parser, new Ltl2baTranslator(), stateCount, threadNumber);
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
        int entryCount = stateCount * buchi.size() * buchi.getAcceptSetsCount();
        ConcurrentIntersectionAutomata<S> automata = new ConcurrentIntersectionAutomata<S>(
                predicates, buchi, entryCount, threadNumber);
        ISharedData sharedData = new SharedData(new ConcurrentHashSet<IntersectionNode>(entryCount, threadNumber),
                                                threadNumber);

        //create threads
        List<DfsThread> threads = new ArrayList<DfsThread>(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threads.add(new DfsThread(null, sharedData, i));
        }
        automata.setThreads(threads);
        ((MultiThreadPredicateFactory) predicates).init(threads);

        IntersectionNode initial = automata.getNode(initState, buchi.getStartNode(), 0);

        startAndWait(threads, initial);

        return extractStack(sharedData.getContraryInstance());
    }

    protected void startAndWait(List<DfsThread> threads, IntersectionNode initial) {
        DfsStackTree<IntersectionNode> stackTree = new DfsStackTree<IntersectionNode>(initial, threadNumber);

        for (DfsThread t: threads) {
            t.setDfsStackTree(stackTree);
            t.start();
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected List<IInterNode> extractStack(DfsStackTreeNode<IntersectionNode> node) {
        LinkedList<IInterNode> res = new LinkedList<IInterNode>();

        for (; node != null; node = node.getParent()) {
            res.addFirst(node.getItem());
        }
        return res;
    }

}
