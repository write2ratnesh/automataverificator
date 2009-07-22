/**
 * CompareTest.java, 12.05.2008
 */
package ru.ifmo.test.verifier.concurrent;

import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;

import java.util.List;
import java.io.IOException;

/**
 * Compare one thread verifier and multi thread
 *
 * @author Kirill Egorov
 */
public class CompareTest extends AbstractCompareTest {

    public CompareTest() throws AutomataFormatException, IOException {
        super("GameA1.xml", "A1");
    }

    public void testOneThread1() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);
        
        assertTrue(stack.isEmpty());
    }

    public void testMultiThread1() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread2() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) || wasEvent(p3.e82))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertFalse(stack.isEmpty());
    }

    public void testMultiThread2() throws LtlParseException {
        String ltlFormula = "G(wasInState(A3, A3.s1) || wasEvent(p3.e82))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertFalse(stack.isEmpty());
    }

    public void testOneThread3() throws LtlParseException {
        String ltlFormula = "G(!isInState(A2, A2[\"PoliceCrash\"]) || R(wasEvent(p3.e81), "
                + "(isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ))))";

        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread3() throws LtlParseException {
        String ltlFormula = "G(!isInState(A2, A2[\"PoliceCrash\"]) || R(wasEvent(p3.e81), "
                + "(isInState(A3, A3.s1) || isInState(A3, A3.stateP) || isInState(A3, A3.stateQ))))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread4() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash)))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread4() throws LtlParseException {
        String ltlFormula = "G(!wasInState(A2, A2.PoliceCrash) || R(wasEvent(p3.e81), wasInState(A2, A2.PoliceCrash)))";
        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

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
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

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
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }
}
