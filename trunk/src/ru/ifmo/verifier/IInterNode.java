/**
 * IInterNode.java, 12.04.2008
 */
package ru.ifmo.verifier;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.buchi.IBuchiNode;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IInterNode {

    IState getState();

    IBuchiNode getNode();

    int getAcceptSet();
}