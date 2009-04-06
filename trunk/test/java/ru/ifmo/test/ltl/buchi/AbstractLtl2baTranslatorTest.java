/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.test.ltl.buchi;

import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.buchi.IBuchiAutomata;

import java.io.IOException;

/**
 * @author kegorov
 *         Date: Apr 6, 2009
 */
public abstract class AbstractLtl2baTranslatorTest extends AbstractTranslatorTest {

    public void testTranslateU() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("U(p1(), p2())");
        System.out.println(buchi);
    }

    public void testTranslateU2() throws IOException, InterruptedException, LtlParseException {
        IBuchiAutomata buchi = extractBuchi("U(false, p2())");
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
}
