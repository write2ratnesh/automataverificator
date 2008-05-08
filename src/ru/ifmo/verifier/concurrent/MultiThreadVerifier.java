/*
 * Developed by eVelopers Corporation - 08.05.2008
 */
package ru.ifmo.verifier.concurrent;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.LtlParseException;

import java.util.List;

public class MultiThreadVerifier<S extends IState> implements IVerifier<S> {
    private int threadNumber;
    private S initState;
    private ILtlParser parser;
    private ITranslator translator = new SimpleTranslator();

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads equals available processors for VM
     * @param initState automata initial state
     */
    public MultiThreadVerifier(S initState) {
        this(initState, null, 0);

    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>
     * @param initState automata initial state
     * @param threadNumber number of threads
     */
    public MultiThreadVerifier(S initState, int threadNumber) {
        this(initState, null, threadNumber);

    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>.
     * @param initState automata initial state
     * @param parser ltl formula parser instance
     * @param threadNumber number of threads
     */
    public MultiThreadVerifier(S initState, ILtlParser parser, int threadNumber) {
        this(initState, parser, new SimpleTranslator(), threadNumber);
    }

    /**
     * Create MultiTreadVerifier that verify automata with <code>initState</code>
     * and uses number of threads = <code>threadNumber</code>
     * @param initState automata initial state
     * @param parser ltl formula parser
     * @param translator ltl to buchi translator
     * @param threadNumber number of threads
     */
    public MultiThreadVerifier(S initState, ILtlParser parser, ITranslator translator, int threadNumber) {
        if (initState == null) {
            throw new IllegalArgumentException("stateMashine can't be null");
        }
        if (translator == null) {
            throw new IllegalArgumentException("translator can't be null");
        }

        this.initState = initState;
        this.parser = parser;
        this.translator = translator;

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

    public List<IInterNode> verify(String ltlFormula, IPredicateUtils<S> predicates) throws LtlParseException {
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

    public List<IInterNode> verify(IBuchiAutomata buchi, IPredicateUtils<S> predicates) {
        return null;  //TODO: implement me!
    }
}
