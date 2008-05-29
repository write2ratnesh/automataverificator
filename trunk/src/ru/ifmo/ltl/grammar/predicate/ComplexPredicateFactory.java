/**
 * ComplexPredicateUtils.java, 02.05.2008
 */
package ru.ifmo.ltl.grammar.predicate;

import ru.ifmo.verifier.automata.statemashine.ComplexState;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexPredicateFactory extends PredicateFactory<ComplexState> {

    @Predicate
    public Boolean isInState(IStateMashine<? extends IState> a, IState s) {
        if (!wasTransition()) {
            return null;
        }
        ComplexState cs = (ComplexState) transition.getTarget();
        return cs.getStateMashineState(a).equals(s);
    }

    @Predicate
    public Boolean wasInState(IStateMashine<? extends IState> a, IState s) {
        return (wasTransition()) ? state.getStateMashineState(a).equals(s): null;
    }
}
