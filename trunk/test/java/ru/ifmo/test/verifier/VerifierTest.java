/**
 * VerifierTest.java, 12.04.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.ltl.LtlParseException;

import java.util.List;

/**
 * Verify A1 automate of CarModel1
 *
 * @author Kirill Egorov
 */
public class VerifierTest extends AbstractSingleAutomataVerifierTest {

    public VerifierTest() {
        super("CarA2.xml", "A1");
    }

    public void testIsInStatePredicate1() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"leave bounds\"]))", predicates);
        assertFalse(stack.isEmpty());
        printStack(stack);
    }

    public void testIsInStatePredicate2() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"do maneuver\"]))", predicates);
        assertFalse(stack.isEmpty());
        printStack(stack);
    }

    public void testWasEventPredicate1() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!wasEvent(p1.e5))", predicates);
        assertFalse(stack.isEmpty());
        printStack(stack);
    }

    public void testWasEventPredicate2() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!wasEvent(p1.e2))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testWasInStatePredicate1() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("R(isInState(A1, A1[\"do maneuver\"]), wasInState(A1, A1[\"move to player\"]))",
                                                 predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testWasInStatePredicate2() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"do maneuver\"]) || wasInState(A1, A1[\"move to player\"]))",
                                                 predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testWasInStatePredicate3() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"do maneuver\"]) " +
                "|| wasInState(A1, A1[\"move to player\"]) || wasInState(A1, A1[\"do maneuver\"]))",
                predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testWasInStatePredicate4() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!(isInState(A1, A1[\"move to player\"]) && wasEvent(p1.e1)) " +
                "|| wasInState(A1, A1[\"create new\"]))",
                predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testWasEvent() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"crash\"])" +
                "|| wasEvent(p1.e5))",
                predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testUUU() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"move to player\"])" +
                "|| R(isInState(A1, A1[\"crash\"]), isInState(A1, A1[\"move to player\"])) " +
                "|| R(isInState(A1, A1[\"leave bounds\"]), isInState(A1, A1[\"move to player\"]))" +
                "|| R(isInState(A1, A1[\"do maneuver\"]), isInState(A1, A1[\"move to player\"])))",
                predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testUU() throws LtlParseException {     
        List<IIntersectionTransition> stack = verifier.verify("G(!(isInState(A1, A1[\"create new\"]) " +
                "|| isInState(A1, A1[\"crash\"]) || isInState(A1, A1[\"s1\"]))" +
                "|| R(wasEvent(p1.e5), wasEvent(p1.e101)) " +
                "|| R(wasEvent(p1.e3), wasEvent(p1.e101)))",
                predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testFutureIsInState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"crash\"])" +
                "|| F(isInState(A1, A1[\"move to player\"])))",
                predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testFutureIsInState2() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1[\"do maneuver\"])" +
                "|| F(isInState(A1, A1[\"move to player\"])))",
                predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }
}
