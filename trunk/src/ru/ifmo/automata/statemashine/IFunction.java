/**
 * IFunction.java, 05.03.2008
 */
package ru.ifmo.automata.statemashine;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IFunction extends IAction {

    public Object getCurValue();

    public Class getReturnType();
}
