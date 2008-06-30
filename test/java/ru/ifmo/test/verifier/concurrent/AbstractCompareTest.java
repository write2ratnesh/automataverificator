/**
 * AbstractCompareTest.java, 11.06.2008
 */
package ru.ifmo.test.verifier.concurrent;

import junit.framework.TestCase;
import ru.ifmo.verifier.automata.statemashine.ComplexState;
import ru.ifmo.verifier.automata.statemashine.ComplexStateFactory;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.concurrent.MultiThreadVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.ComplexPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.automata.statemashine.io.StateMashineReader;

import java.io.IOException;
import java.util.List;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractCompareTest extends TestCase {
    private IAutomataContext context;

    protected final String stateMashineName;

    protected IVerifier<ComplexState> verifier;
    protected IVerifier<ComplexState> verifierMultiThread;
    protected IPredicateFactory<ComplexState> predicates;
    protected IPredicateFactory<ComplexState> predicatesMultiThread;

    protected ILtlParser parser;
    protected ILtlParser parserMultiThread;
    protected ITranslator translator = new SimpleTranslator();

    protected AbstractCompareTest(String xmlFileName, String stateMashineName) throws IOException, AutomataFormatException {
        super();
        this.stateMashineName = stateMashineName;
        context = new AutomataContext(new StateMashineReader(xmlFileName));
    }

    protected void setUp() throws IOException, AutomataFormatException {
        predicates = new ComplexPredicateFactory();
        predicatesMultiThread = new MultiThreadPredicateFactory<ComplexState>(new ComplexPredicateFactory());

        parser = new LtlParser(context, predicates);
        parserMultiThread = new LtlParser(context, predicatesMultiThread);

        IStateMashine<? extends IState> stateMashine = context.getStateMashine(stateMashineName);

        ComplexState initState = ComplexStateFactory.createInitialState(stateMashine);

        verifier = new SimpleVerifier<ComplexState>(initState, parser, translator);
        verifierMultiThread = new MultiThreadVerifier<ComplexState>(initState,
                parserMultiThread, translator, stateMashine.getStates().size());
    }

    protected List<IInterNode> verify(IVerifier<ComplexState> verifier, IBuchiAutomata buchi, IPredicateFactory<ComplexState> predicates) {
        long time = System.currentTimeMillis();
        List<IInterNode> stack = verifier.verify(buchi, predicates);
        time = System.currentTimeMillis() - time;
        System.out.println(getName() + " time = " + time);
        return stack;
    }

    protected IBuchiAutomata parse(ILtlParser parser, String ltlFormula) throws LtlParseException {
        LtlNode ltl = parser.parse(ltlFormula);
        ltl = LtlUtils.getInstance().neg(ltl);
        return translator.translate(ltl);
    }
}
