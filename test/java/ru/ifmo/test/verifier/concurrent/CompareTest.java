/**
 * CompareTest.java, 12.05.2008
 */
package ru.ifmo.test.verifier.concurrent;

import junit.framework.TestCase;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.statemashine.ComplexState;
import ru.ifmo.verifier.automata.statemashine.ComplexStateFactory;
import ru.ifmo.verifier.concurrent.MultiThreadVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.ComplexPredicateFactory;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.buchi.translator.Ltl2baTranslator;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.impl.UnimodXmlReader;

import java.io.IOException;
import java.util.List;

/**
 * Compare one thread verifier and multi thread
 *
 * @author: Kirill Egorov
 */
public class CompareTest extends TestCase {

    protected final String xmlFileName = "GameA1.xml";
    protected final String stateMashineName = "A1";

    protected IVerifier<ComplexState> verifier;
    protected IVerifier<ComplexState> verifierMultiThread;
    protected IPredicateFactory<ComplexState> predicates;
    protected IPredicateFactory<ComplexState> predicatesMultiThread;

    protected ILtlParser parser;
    protected ILtlParser parserMultiThread;
    protected ITranslator translator = new SimpleTranslator();

    protected void setUp() throws IOException, AutomataFormatException {
        predicates = new ComplexPredicateFactory();
        predicatesMultiThread = new MultiThreadPredicateFactory<ComplexState>(new ComplexPredicateFactory());

        IAutomataContext context = new AutomataContext(new UnimodXmlReader(xmlFileName));
        parser = new LtlParser(context, predicates);
        parserMultiThread = new LtlParser(context, predicatesMultiThread);
        IStateMashine<? extends IState> stateMashine = context.getStateMashine(stateMashineName);

        ComplexState initState = ComplexStateFactory.createInitialState(stateMashine);

        verifier = new SimpleVerifier<ComplexState>(initState, parser, translator);
        verifierMultiThread = new MultiThreadVerifier<ComplexState>(initState,
                parserMultiThread, translator, stateMashine.getStates().size());
    }

    public void testOneThread1() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IInterNode> stack = verify(verifier, buchi, predicates);
        
        assertTrue(stack.isEmpty());
    }

    public void testMultiThread1() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IInterNode> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread2() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) || wasEvent(p3.e82))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IInterNode> stack = verify(verifier, buchi, predicates);

        assertFalse(stack.isEmpty());
    }

    public void testMultiThread2() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) || wasEvent(p3.e82))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IInterNode> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertFalse(stack.isEmpty());
    }

    public void testOneThread3() throws LtlParseException {
        String ltlFormula = "G(!isInState(A2, A2[\"PoliceCrash\"]) || R(wasEvent(p3.e81), "
                + "(isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ))))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IInterNode> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread3() throws LtlParseException {
        String ltlFormula = "G(!isInState(A2, A2[\"PoliceCrash\"]) || R(wasEvent(p3.e81), "
                + "(isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ))))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IInterNode> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread4() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash)))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IInterNode> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread4() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash)))";
        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IInterNode> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread5() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash))) &&" +
                "G(!isInState(A2, A2.PoliceCrash) " +
                "|| R(wasEvent(p3.e81), (isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ)))) &&" +
                "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IInterNode> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread5() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash))) &&" +
                "G(!isInState(A2, A2.PoliceCrash) " +
                "|| R(wasEvent(p3.e81), (isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ)))) &&" +
                "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";
        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IInterNode> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    protected List<IInterNode> verify(IVerifier<ComplexState> verifier, IBuchiAutomata buchi, IPredicateFactory<ComplexState> predicates) {
        long time = System.currentTimeMillis();
        List<IInterNode> stack = verifier.verify(buchi, predicates);
        time = System.currentTimeMillis() - time;
        System.out.println(getName() + " time = " + time);
        return stack;
    }

    private IBuchiAutomata parse(ILtlParser parser, String ltlFormula) throws LtlParseException {
        LtlNode ltl = parser.parse(ltlFormula);
        ltl = LtlUtils.getInstance().neg(ltl);
        return translator.translate(ltl);
    }
}
