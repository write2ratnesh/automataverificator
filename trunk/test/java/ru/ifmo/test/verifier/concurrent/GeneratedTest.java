/**
 * GeneratedTest.java, 12.06.2008
 */
package ru.ifmo.test.verifier.concurrent;

import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.automata.statemachine.impl.AutomataFormatException;

import java.util.List;
import java.io.IOException;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class GeneratedTest extends AbstractGeneratedTest {

    protected void setUp() throws Exception {
        System.gc();
    }

    public void testOneThreadA30000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A30000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA30000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A30000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThreadA25000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A25000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA25000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A25000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

     public void testOneThreadA20000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A20000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA20000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A20000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThreadA15000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A15000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA15000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A15000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThreadA10000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A10000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA10000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A10000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThreadA5000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A5000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA5000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A5000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }

    public void testOneThreadA1000() throws AutomataFormatException, IOException, LtlParseException {
        initSimpleVerifier("A1000.xml", "A1");
        String ltlFormula =  "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";
        IBuchiAutomata buchi = parse(parser, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifier, buchi, predicates);

        assertTrue(stack.isEmpty());
    }

    public void testMultiThreadA1000() throws AutomataFormatException, IOException, LtlParseException {
        initMultiThreadVerifier("A1000.xml", "A1");
        String ltlFormula = "G(!wasAction(o1.z49)) && G(!wasEvent(p1.e49))";

        IBuchiAutomata buchi = parse(parserMultiThread, ltlFormula);
        System.out.println("Start verify");
        List<IIntersectionTransition> stack = verify(verifierMultiThread, buchi, predicatesMultiThread);

        assertTrue(stack.isEmpty());
    }
}
