/**
 * CompareTest10000.java, 11.06.2008
 */
package ru.ifmo.test.verifier.concurrent;

import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;

import java.util.List;
import java.io.IOException;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class GeneratedTest5000 extends AbstractCompareTest {
    public GeneratedTest5000() throws AutomataFormatException, IOException {
        super("A30000.xml", "A1");
    }

    public void testOneThread1() throws LtlParseException {
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread1() throws LtlParseException {
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThread2() throws LtlParseException {
        String ltlFormula =  "G(!wasAction(o1.z49)) && "
                + "G(wasInState(A1, A1.s1000) && wasEvent(p1.e29) "
                    + "|| (!isInState(A1, A1.s605) || R(!wasAction(o1.z1), wasAction(o1.z1))) "
                    + "|| (!isInState(A1, A1.s2961) || R(!wasAction(o1.z31), wasAction(o1.z31)))) && "
                + "G(!isInState(A1, A1.s5000) "
                    + "|| X(isInState(A1, A1.s2730)))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThread2() throws LtlParseException {
        String ltlFormula = "G(!wasAction(o1.z49)) && "
                + "G(wasInState(A1, A1.s1000) && wasEvent(p1.e29) "
                    + "|| (!isInState(A1, A1.s605) || R(!wasAction(o1.z1), wasAction(o1.z1))) "
                    + "|| (!isInState(A1, A1.s2961) || R(!wasAction(o1.z31), wasAction(o1.z31)))) && "
                + "G(!isInState(A1, A1.s5000) "
                    + "|| X(isInState(A1, A1.s2730)))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }
}
