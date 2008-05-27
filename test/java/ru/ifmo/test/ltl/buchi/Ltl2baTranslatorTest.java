/*
 * Developed by eVelopers Corporation - 26.05.2008
 */
package ru.ifmo.test.ltl.buchi;

import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.Ltl2baTranslator;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.grammar.LtlNode;

import java.io.IOException;

public class Ltl2baTranslatorTest extends AbstractTranslatorTest {

    public void testGetFormula1() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("F(p1())");
        String f = translator.getFormula(t);

        assertEquals("<>(p1)", f);
    }

    public void testGetFormula2() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("U(p1(), p2())");
        String f = translator.getFormula(t);

        assertEquals("(p1)U(p2)", f);
    }

    public void testGetFormula3() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("R(true, X(p1()))");
        String f = translator.getFormula(t);

        assertEquals("(true)V(X(p1))", f);
    }

    public void testGetFormula4() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("X(wasEvent(p1.e1))");
        String f = translator.getFormula(t);

        assertEquals("X(wasEvente1)", f);
    }

    public void testExecuteLlt2ba() throws IOException, InterruptedException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        String res = translator.executeLlt2ba("((true)U(d))&&((!a)V(b))");

        System.out.println(res);
    }

    public void testTranslateU() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("U(p1(), p2())");
        System.out.println(buchi);
    }

    public void testTranslateR() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("R(p1(), p2())");
        System.out.println(buchi);
    }

    public void testTranslateF() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("F(p1())");
        System.out.println(buchi);
    }

    public void testTranslateG() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("G(p1())");
        System.out.println(buchi);
    }

    public void testTranslateX() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("X(p1())");
        System.out.println(buchi);
    }

    public void testTranslateAnd() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("p1() && p2()");
        System.out.println(buchi);
    }

    public void testTranslateNeg() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("!p1()");
        System.out.println(buchi);
    }

    public void testTranslateNeg2() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("U(!p1(), p2())");
        System.out.println(buchi);
    }

    public void testTranslateTrue() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("true");
        System.out.println(buchi);
    }

    public void testTranslateOr() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("p1() || p2()");
        System.out.println(buchi);
    }

    protected IBuchiAutomata extractBuchi(String expr) throws LtlParseException {
        LtlNode t = parser.parse(expr);
        ITranslator translator = new Ltl2baTranslator();
        return translator.translate(t);
    }
}
