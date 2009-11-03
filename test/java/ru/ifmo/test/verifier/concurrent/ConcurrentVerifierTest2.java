/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.test.verifier.concurrent;

import ru.ifmo.test.verifier.VerifierTest2;
import ru.ifmo.automata.statemachine.IState;
import ru.ifmo.automata.statemachine.IStateMachine;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.concurrent.MultiThreadVerifier;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;

/**
 * @author kegorov
 *         Date: Nov 3, 2009
 */
public class ConcurrentVerifierTest2 extends VerifierTest2 {
    protected IVerifier<IState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new MultiThreadVerifier<IState>(stateMachine.getInitialState(), parser, stateMachine.getStates().size());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
        return new MultiThreadPredicateFactory<IState>(new PredicateFactory<IState>());
    }
}
