/**
 * VerifierTest3.java, 26.04.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.automata.statemachine.IState;

import java.util.List;

/**
 * Verify A1 automata of GameModel
 *
 * @author Kirill Egorov
 */
public abstract class VerifierTest3 extends AbstractVerifierTest<IState> {

    public VerifierTest3() {
        super("GameA1.xml", "A1");
    }

    public void testGlobalIsInState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1.s2))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalCameToFinalState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!cameToFinalState())", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testGlobalWasEvent() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!wasEvent(p2.e4))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testFutureCameToFinalState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("F(isInState(A1, A1.s2))", predicates);
        printStack(stack);
        assertFalse(stack.isEmpty());
    }

    public void testNextDoWaitState() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1.Pause) || !X(isInState(A1, A1.doWait)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }

    public void testFutureStart() throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify("G(!isInState(A1, A1.Pause) || F(isInState(A1, A1.Start) || isInState(A1, A1.s2)))", predicates);
        printStack(stack);
        assertTrue(stack.isEmpty());
    }
}
