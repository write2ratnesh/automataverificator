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
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Ltl2baTranslatorTest extends AbstractTranslatorTest {

    public void testGetFormula1() throws LtlParseException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        LtlNode t = parser.parse("F(p1())");
        String f = translator.getFormula(t);

        assertEquals("F(p1)", f);
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

        assertEquals("(true)R(X(p1))", f);
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

    public void testN() throws IOException, InterruptedException {
        Ltl2baTranslator translator = new Ltl2baTranslator();
        String res = translator.executeLlt2ba("(a U b) || (Xa) && (d U e)");
        System.out.println(res);

        Matcher m = Pattern.compile("T\\d+_S\\d+:\\s+if(\\s+::.*)+\\s+fi;").matcher(res);
        m.find();
        String state = res.substring(m.start(), m.end());
        Matcher m2 = Pattern.compile("::.*").matcher(state);
        m2.find();
        System.out.println(state.substring(m2.start(), m2.end()));


        String[] arr = "a && b && !c".split("&&");
        System.out.println(arr.length);
    }


    protected IBuchiAutomata extractBuchi(String expr) throws LtlParseException {
        LtlNode t = parser.parse(expr);
        ITranslator translator = new Ltl2baTranslator();
        return translator.translate(t);
    }
}
