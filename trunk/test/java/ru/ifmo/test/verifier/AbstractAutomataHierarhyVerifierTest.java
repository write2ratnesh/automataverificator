/**
 * AbstractAutomataHierarhyVerifierTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.automata.statemashine.ComplexStateFactory;
import ru.ifmo.verifier.automata.statemashine.ComplexState;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.ComplexPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractAutomataHierarhyVerifierTest extends AbstractVerifierTest<ComplexState> {

    protected AbstractAutomataHierarhyVerifierTest(String xmlFileName, String stateMashineName) {
        super(xmlFileName, stateMashineName);
    }

    protected IVerifier<ComplexState> createVerifier(IStateMashine<? extends IState> stateMashine, ILtlParser parser) {
        return new SimpleVerifier<ComplexState>(ComplexStateFactory.createInitialState(stateMashine), parser);
    }

    protected IPredicateFactory<ComplexState> createPredicateUtils() {
        return new ComplexPredicateFactory();
    }
}
