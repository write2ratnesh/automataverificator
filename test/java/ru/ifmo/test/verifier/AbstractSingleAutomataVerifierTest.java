/**
 * AbstractSingleAutomataTest.java, 01.05.2008
 */
package ru.ifmo.test.verifier;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.impl.SimpleVerifier;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;
import ru.ifmo.ltl.grammar.predicate.PredicateUtils;

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
    }

    protected IPredicateUtils<IState> createPredicateUtils() {
        return new PredicateUtils<IState>();
    }
}
