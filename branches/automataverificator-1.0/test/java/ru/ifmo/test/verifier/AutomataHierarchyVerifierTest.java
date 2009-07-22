/**
 * AutomataHierarchyTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.verifier.IInterNode;

import java.util.List;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class AutomataHierarchyVerifierTest extends AbstractAutomataHierarhyVerifierTest {
    public AutomataHierarchyVerifierTest() {
        super("GameA1.xml", "A1");
    }

    public void testGlobalIsInState() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(!isInState(A1, A1.s2))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalCameToFinalState() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(!cameToFinalState())", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(!wasEvent(p2.e4))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent1() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(isInState(A3, A3.s1) || wasEvent(p3.e82))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent2() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(isInState(A3, A3.s1) || X(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent3() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(wasInState(A3, A3.s1) || wasEvent(p3.e82))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent4() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(wasInState(A3, A3.s1) || G(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testReleaseWasAction() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testFutureStart() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(!isInState(A1, A1.Pause) || F(isInState(A1, A1.Start) || isInState(A1, A1.s2)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testGlobalIsInState2() throws LtlParseException {
        List<IInterNode> stack = verifier.verify("G(!wasInState(A3, A3.stateP) || X(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }
}
