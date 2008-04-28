/**
 * IVerifier.java, 06.04.2008
 */
package ru.ifmo.verifier;

import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;

import java.util.List;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IVerifier {

    List<IInterNode> verify(IBuchiAutomata buchi, IPredicateUtils predicates);

    List<IInterNode> verify(String ltlFormula, IPredicateUtils predicates) throws LtlParseException;
}
