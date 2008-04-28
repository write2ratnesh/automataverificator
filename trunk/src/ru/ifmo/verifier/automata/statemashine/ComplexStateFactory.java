/**
 * ComplexStateFactory.java, 27.04.2008
 */
package ru.ifmo.verifier.automata.statemashine;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;
import ru.ifmo.verifier.automata.tree.ITree;
import org.apache.commons.lang.NotImplementedException;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class ComplexStateFactory implements IComplexStateFactory {

    public IState getState(ITree<IState> tree) {
        throw new NotImplementedException(); //TODO
    }

    public IState addState(ComplexState cs, IStateTransition trans) {
        throw new NotImplementedException(); //TODO
    }
}
