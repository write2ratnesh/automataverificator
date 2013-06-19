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
        test("G( !wasEvent(ep.nfv5) or ( wasAction(co.mr1) and wasAction(co.mr2) and wasAction(co.mr3) ) )");
    }

    public void test2() throws LtlParseException {
        test("G( !wasEvent(ep.nfv9) or ( wasAction(co.mr1) and wasAction(co.mr2) and wasAction(co.mr3) ) )");
    }

    public void test3() throws LtlParseException {
        test("G( !wasEvent(ep.c2_next) or wasAction(co.c1_top10) )");
    }

    public void test4() throws LtlParseException {
        test("G( !wasEvent(ep.c2_empty) or ( wasAction(co.c1_clean) and wasAction(co.t_reset) ) )");
    }

    public void test5() throws LtlParseException {
        test("G( !wasEvent(ep.nt) or X(wasEvent(ep.nfv5) or wasEvent(ep.nfv9)) )");
    }

    public void test6() throws LtlParseException {
        test("G( !wasEvent(ep.nfv5) or " +
                " X(wasEvent(ep.tmpl) and wasAction(co.mc1) and wasAction(co.mc2) and wasAction(co.mc3) " +
                " and X( wasEvent(ep.tmpl) and wasAction(co.mc1) )) )");
    }

    public void test7() throws LtlParseException {
        test("G( !wasEvent(ep.t) or X(G(wasEvent(ep.c2_next)) or U(wasEvent(ep.c2_next), wasEvent(ep.c2_empty)) ) )");
    }

    public void test8() throws LtlParseException {
        test("G( !wasEvent(ep.nfv5) or " +
                " X(R( wasEvent(ep.nt) or wasEvent(ep.c2_empty), !wasEvent(ep.nfv5) and !wasEvent(ep.nfv9) )) )");
    }

    public void test9() throws LtlParseException {
        test("G( !X(wasEvent(ep.tmpl)) or (wasAction(co.mr1) or wasAction(co.mc2)) )");
    }

    private void test(String formula) throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify(formula, predicates);

        printStack(stack);
        assertTrue(stack.isEmpty());
    }
}
