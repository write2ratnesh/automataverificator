/*
 * Developed by eVelopers Corporation - 27.05.2008
 */
package ru.ifmo.ltl.buchi.impl;

import ru.ifmo.ltl.buchi.ITransitionCondition;
import ru.ifmo.ltl.grammar.IExpression;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Member;

import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;
import org.apache.commons.lang.BooleanUtils;

public class OgnlTransitionCondition implements ITransitionCondition {

    private String cond;
    private Object tree;
    private Map root = new HashMap();
    private Map context;

    public OgnlTransitionCondition(String cond, Map<String, IExpression<Boolean>> exprs) throws OgnlException {
        this.cond = cond.trim();
        tree = Ognl.parseExpression(this.cond);
        this.root = exprs;

        context = Ognl.createDefaultContext(root);
        Ognl.setTypeConverter(context, new TypeConverter() {
            public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
                if (toType.equals(boolean.class) || toType.equals(Boolean.class)) {
                    if (value instanceof IExpression) {
                        return ((IExpression) value).getValue();
                    } else if (value instanceof Number) {
                        return ((Number) value).intValue() == 1;
                    } else if (value instanceof Boolean) {
                        return value;
                    }
                }
                return null;
            }
        });
    }

    @Deprecated
    public Set<IExpression<Boolean>> getExpressions() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public Set<IExpression<Boolean>> getNegExpressions() {
        throw new UnsupportedOperationException();
    }

    public boolean getValue() {
        try {
            Boolean res = (Boolean) Ognl.getValue(tree, context, root, Boolean.class);
            return BooleanUtils.isTrue(res);
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return cond;
    }
}
