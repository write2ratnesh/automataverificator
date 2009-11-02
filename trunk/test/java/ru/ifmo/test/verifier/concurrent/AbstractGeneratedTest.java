/**
 * AbstractGeneratedTest.java, 12.06.2008
 */
package ru.ifmo.test.verifier.concurrent;

import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.verifier.concurrent.MultiThreadVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMachine;
import ru.ifmo.automata.statemashine.io.StateMachineReader;

import java.util.List;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public abstract class AbstractGeneratedTest extends TestCase {
    private IAutomataContext context;

    protected IVerifier<IState> verifier;
    protected IVerifier<IState> verifierMultiThread;
    protected IPredicateFactory<IState> predicates;
    protected IPredicateFactory<IState> predicatesMultiThread;

    protected ILtlParser parser;
    protected ILtlParser parserMultiThread;
    protected ITranslator translator = new SimpleTranslator();

    protected void initSimpleVerifier(String xmlFileName, String stateMashineName) throws IOException, AutomataFormatException {
        predicates = new PredicateFactory<IState>();

        context = new AutomataContext(new StateMachineReader(xmlFileName));
        parser = new LtlParser(context, predicates);

        IStateMachine<? extends IState> stateMachine = context.getStateMachine(stateMashineName);

        IState initState = stateMachine.getInitialState();

        verifier = new SimpleVerifier<IState>(initState, parser, translator);
    }

    protected void initMultiThreadVerifier(String xmlFileName, String stateMashineName) throws IOException, AutomataFormatException {
        predicatesMultiThread = new MultiThreadPredicateFactory<IState>(new PredicateFactory<IState>());

        context = new AutomataContext(new StateMachineReader(xmlFileName));
        parserMultiThread = new LtlParser(context, predicatesMultiThread);

        IStateMachine<? extends IState> stateMachine = context.getStateMachine(stateMashineName);

        IState initState = stateMachine.getInitialState();

        verifierMultiThread = new MultiThreadVerifier<IState>(initState,
                parserMultiThread, translator, stateMachine.getStates().size());
    }

    protected List<IIntersectionTransition> verify(IVerifier<IState> verifier, IBuchiAutomata buchi,
                                      IPredicateFactory<IState> predicates) {
        long time = System.currentTimeMillis();
        List<IIntersectionTransition> stack = verifier.verify(buchi, predicates);
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
