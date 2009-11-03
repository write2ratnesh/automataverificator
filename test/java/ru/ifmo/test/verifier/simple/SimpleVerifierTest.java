/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.test.verifier.simple;

import ru.ifmo.test.verifier.VerifierTest;
import ru.ifmo.automata.statemachine.IState;
import ru.ifmo.automata.statemachine.IStateMachine;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;

/**
 * @author kegorov
 *         Date: Nov 3, 2009
 */
public class SimpleVerifierTest extends VerifierTest {
    protected IVerifier<IState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new SimpleVerifier<IState>(stateMachine.getInitialState(), parser, new JLtl2baTranslator());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
        return new PredicateFactory<IState>();
    }
}
