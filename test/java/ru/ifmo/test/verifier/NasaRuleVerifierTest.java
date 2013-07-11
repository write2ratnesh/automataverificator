/* 
 * Developed by eVelopers Corporation, 2013
 */
package ru.ifmo.test.verifier;

import ru.ifmo.automata.statemachine.IState;
import ru.ifmo.automata.statemachine.IStateMachine;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.verifier.impl.SimpleVerifier;

import java.util.List;

/**
 * @author kegorov
 *         Date: 6/19/13
 */
public class NasaRuleVerifierTest extends AbstractVerifierTest<IState> {

    public NasaRuleVerifierTest() {
        super("nasa_rule.xml", "A1");
    }

    protected IVerifier<IState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new SimpleVerifier<IState>(stateMachine.getInitialState(), parser, new JLtl2baTranslator());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
        return new PredicateFactory<IState>();
    }

    public void test1() throws LtlParseException {
        test("G( !wasEvent(ep.nfv5) or wasAction(co.mr1) )");
    }

    public void test2() throws LtlParseException {
        test("G( !wasEvent(ep.nfv9) or wasAction(co.mr2) )");
    }

    public void test3() throws LtlParseException {
        test("G( !wasEvent(ep.c2_next) or wasAction(co.c1_exp) )");
    }

    public void test4() throws LtlParseException {
        test("G( !wasEvent(ep.c2_empty) or ( wasAction(co.c1_clean) and wasAction(co.c2_clean) and wasAction(co.t_reset) ) )");
    }

    public void test5() throws LtlParseException {
        test("G( !(wasEvent(ep.c2_empty) or wasAction(co.mc2)) or X(wasEvent(ep.nfv5) or wasEvent(ep.nfv9) or wasEvent(ep.t)) )");
    }

    public void test6() throws LtlParseException {
        test("G( !wasEvent(ep.e1) or wasAction(co.sl) )");
    }

    public void test8() throws LtlParseException {
        test("G( !wasEvent(ep.c2_next) or X( G(wasEvent(ep.e1)) or U(wasEvent(ep.e1), wasEvent(ep.e2)) ) )");
    }

    public void test9() throws LtlParseException {
        test("G( !(wasEvent(ep.nfv5) or wasEvent(ep.nfv9)) or " +
                " X(R( wasEvent(ep.tmpl) and wasAction(co.mc2), !wasEvent(ep.nfv5) and !wasEvent(ep.nfv9) and !wasEvent(ep.t) )) )");
    }

    public void test10() throws LtlParseException {
        test("G( !wasEvent(ep.t) or " +
                " X(R( wasEvent(ep.c2_empty), !wasEvent(ep.nfv5) and !wasEvent(ep.nfv9) and !wasEvent(ep.t) )) )");
    }

    private void test(String formula) throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify(formula, predicates);

        printStack(stack);
        assertTrue(stack.isEmpty());
    }
}
