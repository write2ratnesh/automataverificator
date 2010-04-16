/* 
 * Developed by eVelopers Corporation, 2010
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
 *         Date: Apr 15, 2010
 */
public class BankomatVerifierTest extends AbstractVerifierTest<IState> {

    public BankomatVerifierTest() {
        super("bankomat.xml", "A1");
    }

    protected IVerifier<IState> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser) {
        return new SimpleVerifier<IState>(stateMachine.getInitialState(), parser, new JLtl2baTranslator());
    }

    protected IPredicateFactory<IState> createPredicateUtils() {
        return new PredicateFactory<IState>();
    }

    public void test1() throws LtlParseException {
        test("G( !wasEvent(ep.IC) or wasAction(co.z1) )");
    }

    public void test2() throws LtlParseException {
        test("G( ( !wasEvent(ep.EC) or wasAction(co.z2) ) and ( !wasAction(co.z2) or wasEvent(ep.EC) ) )");
    }

    public void test3() throws LtlParseException {
        test("G( ( !wasEvent(ep.A) or wasAction(co.z3) ) and ( !wasAction(co.z3) or wasEvent(ep.A) ) )");
    }

    public void test4() throws LtlParseException {
        test("G( !wasEvent(ep.AE) or wasAction(co.z1) )");
    }

    public void test5() throws LtlParseException {
        test("G( !wasEvent(ep.AS) or wasAction(co.z4) )");
    }

    public void test6() throws LtlParseException {
// TODO!
        test("G(!wasEvent(ep.IC) or X( R(wasEvent(ep.EC), !wasEvent(ep.IC))))");
    }

    public void test7() throws LtlParseException {
        test("G(!wasEvent(ep.EC) or X(wasEvent(ep.IC)))");
    }

    public void test8() throws LtlParseException {
        test("G(!wasEvent(ep.IC) or X( wasEvent(ep.EC) or wasEvent(ep.A) ))");
    }

    public void test9() throws LtlParseException {
// TODO:       
        test("R(wasEvent(ep.AS), !(wasAction(co.z4) && !wasEvent(ep.AS)))");
    }

    public void test10() throws LtlParseException {
// TODO:
        test("R(wasEvent(ep.A), !(wasEvent(ep.AS) or wasEvent(ep.AE)))");
    }

// group 2

    public void test11() throws LtlParseException {
        test("G( ( !wasEvent(ep.C) or wasAction(co.z5) ) and ( !wasAction(co.z5) or wasEvent(ep.C) ) )");
    }

    public void test12() throws LtlParseException {
        test("G( ( !wasEvent(ep.CR) or wasAction(co.z6) ) and ( !wasAction(co.z6) or wasEvent(ep.CR) ) )");
    }

    public void test13() throws LtlParseException {
        test("G( ( !wasEvent(ep.CP) or (wasAction(co.z7) and wasAction(co.z4)) ) and ( !wasAction(co.z7) or wasEvent(ep.CP) ) )");
    }

    public void test14() throws LtlParseException {
        test("G(!wasEvent(ep.C) or F(wasAction(co.z4)))");
    }

    public void test15() throws LtlParseException {
        test("G(!wasEvent(ep.C) or X(wasEvent(ep.CR)))");
    }

    public void test16() throws LtlParseException {
        test("G(!wasEvent(ep.CR) or X( wasEvent(ep.CNL) or wasEvent(ep.CP) ))");
    }

// group 3

    public void test17() throws LtlParseException {
        test("G(!wasEvent(ep.M) or wasAction(co.z8))");
    }

    public void test18() throws LtlParseException {
        test("G( ( !wasEvent(ep.MR) or wasAction(co.z9) ) and ( !wasAction(co.z9) or wasEvent(ep.MR) ) )");
    }

    public void test19() throws LtlParseException {
        test("G(!wasEvent(ep.ME) or wasAction(co.z8))");
    }

    public void test20() throws LtlParseException {
        test("G( (!wasEvent(ep.MS) or wasAction(co.z10)) and (!wasAction(co.z10) or wasEvent(ep.MS)) )");
    }

    public void test21() throws LtlParseException {
        test("G( (!wasEvent(ep.MP) or ( wasAction(co.z4) and wasAction(co.z11) and wasAction(co.z12) and wasAction(co.z13) ))\n" +
                "                and (!wasAction(co.z13) or wasEvent(ep.MP)))");
    }

    public void test22() throws LtlParseException {
        test("G(!wasEvent(ep.MS) or F(wasAction(co.z4) and wasAction(co.z11) and wasAction(co.z12)))");
    }

    public void test23() throws LtlParseException {
        test("G( (!wasAction(co.z11) or wasAction(co.z12)) and (!wasAction(co.z12) or wasAction(co.z11)) )");
    }

    public void test24() throws LtlParseException {
// TODO:
        test("G( !wasEvent(ep.MS) or F(wasAction(co.z11)) )");
    }

    public void test25() throws LtlParseException {
        test("G(!wasEvent(ep.M) or X( wasEvent(ep.MR) or wasEvent(ep.CNL) ))");
    }

    public void test26() throws LtlParseException {
        test("G(!wasEvent(ep.MR) or X( wasEvent(ep.MS) or wasEvent(ep.ME) ))");
    }

    public void test27() throws LtlParseException {
        test("G(!wasEvent(ep.ME) or X( wasEvent(ep.MR) or wasEvent(ep.CNL) ))");
    }

    public void test28() throws LtlParseException {
        test("G(!wasEvent(ep.MS) or X( wasEvent(ep.CNL) or wasEvent(ep.MP) ))");
    }


// group 4

    public void test29() throws LtlParseException {
        test("G(!wasEvent(ep.CNL) or wasAction(co.z4))");
    }

    public void test30() throws LtlParseException {
        test("G(!X(wasEvent(ep.EC)) or (wasAction(co.z4) or wasAction(co.z1)))");
    }



    private void test(String formula) throws LtlParseException {
        List<IIntersectionTransition> stack = verifier.verify(formula, predicates);

        printStack(stack);
        assertTrue(stack.isEmpty());
    }
}
