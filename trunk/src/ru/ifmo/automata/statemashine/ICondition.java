/**
 * ICondition.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine;

import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface ICondition {

    public String getExpression();

    public boolean evaluate(Object source);
}
