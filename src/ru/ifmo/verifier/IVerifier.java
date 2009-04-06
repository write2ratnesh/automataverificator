/**
 * IVerifier.java, 06.04.2008
 */
package ru.ifmo.verifier;

import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.automata.statemashine.IState;

import java.util.List;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface IVerifier<S extends IState> {

    List<IInterNode> verify(IBuchiAutomata buchi, IPredicateFactory<S> predicates);

    List<IInterNode> verify(String ltlFormula, IPredicateFactory<S> predicates) throws LtlParseException;
}
