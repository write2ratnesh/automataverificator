/**
 * OgnlSimpleTest.java, 05.03.2008
 */
package ru.ifmo.test.ognl;

import junit.framework.TestCase;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlContext;
import ognl.DefaultTypeConverter;

import java.util.Map;
import java.util.HashMap;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class OgnlSimpleTest extends TestCase {

    public void testParsExpr() throws OgnlException {
        Object tree = Ognl.parseExpression("o1.x1 && o1.x2");

        Map context = new HashMap();
        Map<String, Boolean> o1 = new HashMap<String, Boolean>();
        o1.put("x1", true);
        o1.put("x2", true);
        context.put("o1", o1);
//        context.put(OgnlContext.TYPE_CONVERTER_CONTEXT_KEY, new DefaultTypeConverter());
        Boolean b = (Boolean) Ognl.getValue(tree, (Object) context, Boolean.class);
        assertTrue(b);
    }

    public void testParsExpr2() throws OgnlException {
        Object tree = Ognl.parseExpression("o1.x1 || o1.x2");

        Map context = new HashMap();
        Map<String, Boolean> o1 = new HashMap<String, Boolean>();
        o1.put("x1", false);
        o1.put("x2", true);
        context.put("o1", o1);
        Boolean b = (Boolean) Ognl.getValue(tree, (Object) context, Boolean.class);
        assertTrue(b);
    }

    public void testParsExpr3() throws OgnlException {
        Object tree = Ognl.parseExpression("o1.x1 || o1.x2");

        Map context = new HashMap();
        Map<String, Boolean> o1 = new HashMap<String, Boolean>();
        o1.put("x1", false);
        o1.put("x2", false);
        context.put("o1", o1);
        Boolean b = (Boolean) Ognl.getValue(tree, (Object) context, Boolean.class);
        assertFalse(b);
    }
}
