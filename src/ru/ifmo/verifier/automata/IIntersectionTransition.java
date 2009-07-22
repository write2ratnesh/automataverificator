/* 
 * Developed by eVelopers Corporation, 2009
 */
package ru.ifmo.verifier.automata;

import ru.ifmo.automata.ITransition;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.automata.statemashine.IState;

/**
 * @author kegorov
 *         Date: Jul 22, 2009
 */
public interface IIntersectionTransition<S extends IState> extends ITransition<IntersectionNode<S>> {
    IStateTransition getTransition();

    IntersectionNode<S> getTarget();
}
