/**
 * ComplexPredicateUtils.java, 02.05.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.verifier.automata.statemashine.ComplexTransition;
import ru.ifmo.verifier.automata.statemashine.ComplexState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexPredicateUtils extends PredicateUtils<ComplexState> {

    @Predicate
    public boolean isInState(IStateMashine<? extends IState> a, IState s) {
        ComplexState cs = (ComplexState) transition.getTarget();
        return cs.getStateMashineState(a).equals(s);
    }

    @Predicate
    public boolean wasInState(IStateMashine<? extends IState> a, IState s) {
        return state.getStateMashineState(a).equals(s);
    }
}
