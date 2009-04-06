/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.test.ltl.buchi;

import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.Ltl2baTranslator;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.LtlParseException;

/**
 * TODO: write more tests
 * @author kegorov
 *         Date: Apr 6, 2009
 */
public class Ltl2baCompareTest extends AbstractTranslatorTest {

    public void test1() throws LtlParseException {
        String expr = "U(p1(), R(p2(), !p3()) && F(p4()))";
        LtlNode node = parser.parse(expr);
        ITranslator ltl2ba = new Ltl2baTranslator();
        ITranslator ltl2ba4j = new JLtl2baTranslator();

        translate(ltl2ba, node);
        translate(ltl2ba4j, node);
    }

    public void test2() throws LtlParseException {
        String expr = "R(G(p1()), F(U(!p2() && X(X(p4())), X(p3()))))";
        LtlNode node = parser.parse(expr);
        ITranslator ltl2ba = new Ltl2baTranslator();
        ITranslator ltl2ba4j = new JLtl2baTranslator();

        translate(ltl2ba, node);
        translate(ltl2ba4j, node);
    }

    protected void translate(ITranslator t, LtlNode node) {
        long time = System.currentTimeMillis();
        t.translate(node);
        time = System.currentTimeMillis() - time;
        System.out.println(t.getClass().getName() + " time = " + time);
    }

    protected ITranslator getTranslator() {
        return null;
    }
}
