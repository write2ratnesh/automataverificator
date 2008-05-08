package ru.ifmo.verifier.automata;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;

public interface IIntersectionAutomata<S extends IState> {

    IBuchiAutomata getBuchiAutomata();

    IPredicateUtils<S> getPredicates();

    IntersectionNode<S> getNode(S state, IBuchiNode node, int acceptSet);
}
