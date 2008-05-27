/**
 * AbstractSingleAutomataTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.verifier.concurrent.MultiThreadVerifier;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.grammar.predicate.MultiThreadPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractSingleAutomataVerifierTest extends AbstractVerifierTest<IState> {
    protected AbstractSingleAutomataVerifierTest(String xmlFileName, String stateMashineName) {
        super(xmlFileName, stateMashineName);
    }

    protected IVerifier<IState> createVerifier(IStateMashine<? extends IState> stateMashine, ILtlParser parser) {
        return new SimpleVerifier<IState>(stateMashine.getInitialState(), parser);
//        return new MultiThreadVerifier<IState>(stateMashine.getInitialState(), parser, stateMashine.getStates().size());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
//        return new MultiThreadPredicateFactory<IState>(new PredicateFactory<IState>());
        return new PredicateFactory<IState>();
    }
}
