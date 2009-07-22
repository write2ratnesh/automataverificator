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
import java.util.regex.Pattern;

public class Ltl2baTranslatorTest extends AbstractLtl2baTranslatorTest {

    public void testGetFormula1() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("F(p1())");
        String f = translator.getFormula(t);

        assertTrue(f, Pattern.matches("<>\\(p1\\)", f));
    }

    public void testGetFormula2() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("U(p1(), p2())");
        String f = translator.getFormula(t);

        assertTrue(f, Pattern.matches("\\(p1\\)U\\(p2\\)", f));
    }

    public void testGetFormula3() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("R(true, X(p1()))");
        String f = translator.getFormula(t);

        assertTrue(f, Pattern.matches("\\(true\\)V\\(X\\(p1\\)\\)", f));
    }

    public void testGetFormula4() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("X(wasEvent(p1.e1))");
        String f = translator.getFormula(t);

        assertTrue(f, Pattern.matches("X\\(wasEvente1\\)", f));
    }

    public void testExecuteLlt2ba() throws IOException, InterruptedException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        String res = translator.executeLlt2ba("((true)U(d))&&((!a)V(b))");

        System.out.println(res);
    }

    protected ITranslator getTranslator() {
        return new Ltl2baTranslator();
    }
}
