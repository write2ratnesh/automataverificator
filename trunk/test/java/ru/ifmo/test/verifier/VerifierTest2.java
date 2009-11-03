/**
 * VerifierTest2.java, 26.04.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemachine.IState;

import java.util.List;

/**
 * Verify A3 automata of GameModel
 *
 * @author Kirill Egorov
 */
public abstract class VerifierTest2 extends AbstractVerifierTest<IState> {

    public VerifierTest2() {
        super("GameA1.xml", "A3");
    }

    public void testGlobalWasEvent1() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(isInState(A3, A3.s1) || wasEvent(p3.e82))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent2() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(isInState(A3, A3.s1) || X(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testGlobalWasEvent3() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(wasInState(A3, A3.s1) || wasEvent(p3.e82))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testGlobalWasEvent4() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(wasInState(A3, A3.s1) || G(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testGlobalIsInState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!wasInState(A3, A3.stateP) || X(wasEvent(p3.e82)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testReleaseWasAction() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(wasInState(A3, A3.s1) "
                + "|| (!isInState(A3, A3.stateP) || R(wasAction(o2.z11), wasAction(o2.z10))) "
                + "|| (!isInState(A3, A3.stateQ) || R(wasAction(o2.z10), wasAction(o2.z11))))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }
}
