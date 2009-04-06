/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.test.ltl.buchi;

import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.JLtl2baTranslator;

/**
 * @author kegorov
 *         Date: Apr 6, 2009
 */
public class Ltl2ba4jTranslatorTest extends AbstractLtl2baTranslatorTest {
    protected ITranslator getTranslator() {
        return new JLtl2baTranslator();
    }
}
