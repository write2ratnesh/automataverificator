/**
 * AbstractSingleAutomataTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.automata.statemachine.IState;
import ru.ifmo.automata.statemachine.IStateMachine;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public abstract class AbstractSingleAutomataVerifierTest extends AbstractVerifierTest<IState> {
    protected AbstractSingleAutomataVerifierTest(String xmlFileName, String stateMachineName) {
        super(xmlFileName, stateMachineName);
    }

    protected IVerifier<IState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new SimpleVerifier<IState>(stateMachine.getInitialState(), parser, new JLtl2baTranslator());
//        return new MultiThreadVerifier<IState>(stateMachine.getInitialState(), parser, stateMachine.getStates().size());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
//        return new MultiThreadPredicateFactory<IState>(new PredicateFactory<IState>());
        return new PredicateFactory<IState>();
    }
}
