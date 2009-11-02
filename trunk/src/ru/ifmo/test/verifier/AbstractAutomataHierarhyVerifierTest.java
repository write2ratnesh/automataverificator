/**
 * AbstractAutomataHierarhyVerifierTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.automata.statemachine.ComplexStateFactory;
import ru.ifmo.verifier.automata.statemachine.ComplexState;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.automata.statemachine.IState;
import ru.ifmo.automata.statemachine.IStateMachine;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.ComplexPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public abstract class AbstractAutomataHierarhyVerifierTest extends AbstractVerifierTest<ComplexState> {

    protected AbstractAutomataHierarhyVerifierTest(String xmlFileName, String stateMachineName) {
        super(xmlFileName, stateMachineName);
    }

    protected IVerifier<ComplexState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new SimpleVerifier<ComplexState>(ComplexStateFactory.createInitialState(stateMachine), parser, new JLtl2baTranslator());
    }

    protected IPredicateFactory<ComplexState> createPredicateUtils() {
        return new ComplexPredicateFactory();
    }
}
