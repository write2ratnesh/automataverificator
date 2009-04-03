/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.ltl.buchi.translator;

import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.grammar.LtlNode;

/**
 * Ltl formula translator to Buchi automata.
 * Used ltl2ba4j library to call LTL2BA. Ltl2ba is called using JNI.
 * <br>Visit <a href="http://www.sable.mcgill.ca/~ebodde/rv//ltl2ba4j/">ltl2ba4j site</a> for
 * more information abount this library.
 *
 * @author kegorov
 *         Date: Apr 3, 2009
 */
public class JLtl2baTranslator implements ITranslator {
    public IBuchiAutomata translate(LtlNode root) {
        return null;  //TODO: implement me!
    }
}
