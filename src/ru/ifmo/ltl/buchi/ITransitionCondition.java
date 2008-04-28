/**
 * ITransitionCondition.java, 16.03.2008
 */
package ru.ifmo.ltl.buchi;

import ru.ifmo.ltl.grammar.Predicate;
import ru.ifmo.ltl.grammar.IExpression;

import java.util.Set;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface ITransitionCondition {

    public Set<IExpression<Boolean>> getExpressions();

    public Set<IExpression<Boolean>> getNegExpressions();

    public boolean getValue();
}
