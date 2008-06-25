/**
 * LtlUtilsTest.java, 22.03.2008
 */
package ru.ifmo.test.ltl;

import junit.framework.TestCase;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.io.UnimodXmlReader;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.ltl.grammar.*;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.LtlParseException;

import java.io.IOException;

/**
 * Test LtlUtils
 *
 * @author: Kirill Egorov
 */
public class LtlUtilsTest extends TestCase {

    private ILtlParser parser;

    protected void setUp() throws IOException, AutomataFormatException {
        PredicateFactory predicates = new PredicateFactory();
        IAutomataContext context = new AutomataContext(new UnimodXmlReader("CarA1.xml"));
        parser = new LtlParser(context, predicates);
    }

    public void testNormalizationAbsence() throws LtlParseException {
        LtlNode t = parser.parse("R(isInState(A1, A1.s1), false)");
        LtlNode norm = LtlUtils.getInstance().normalize(t);
        assertEquals(t, norm);

        assertTrue(BinaryOperator.class.isAssignableFrom(norm.getClass()));
        BinaryOperator op = (BinaryOperator) norm;
        assertEquals(BinaryOperatorType.RELEASE, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertEquals(BooleanNode.FALSE, op.getRightOperand());
    }

    public void testNormalization1() throws LtlParseException {
        LtlNode t = parser.parse("F(false)");
        t = LtlUtils.getInstance().normalize(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertEquals(BooleanNode.FALSE, op.getRightOperand());

    }

    public void testNormalization2() throws LtlParseException {
        LtlNode t = parser.parse("F(R(isInState(A1, A1.s1), true))");
        t = LtlUtils.getInstance().normalize(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertTrue(BinaryOperator.class.isAssignableFrom(op.getRightOperand().getClass()));
        op = (BinaryOperator) op.getRightOperand();
        assertEquals(BinaryOperatorType.RELEASE, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertEquals(BooleanNode.TRUE, op.getRightOperand());
    }

    public void testNormalization3() throws LtlParseException {
        LtlNode t = parser.parse("R(isInState(A1, A1.s1), F(false))");
        t = LtlUtils.getInstance().normalize(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.RELEASE, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertTrue(BinaryOperator.class.isAssignableFrom(op.getRightOperand().getClass()));
        op = (BinaryOperator) op.getRightOperand();
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertEquals(BooleanNode.FALSE, op.getRightOperand());
    }

    public void testNormalization4() throws LtlParseException {
        LtlNode t = parser.parse("R(F(false), isInState(A1, A1.s1))");
        t = LtlUtils.getInstance().normalize(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.RELEASE, op.getType());
        assertEquals("isInState", op.getRightOperand().getName());
        assertTrue(BinaryOperator.class.isAssignableFrom(op.getLeftOperand().getClass()));
        op = (BinaryOperator) op.getLeftOperand();
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertEquals(BooleanNode.FALSE, op.getRightOperand());
    }

    public void testNnfAbsence1() throws LtlParseException {
        LtlNode t = parser.parse("false");
        LtlNode nnf = LtlUtils.getInstance().toNnf(t);
        assertEquals(t, nnf);
        assertEquals(BooleanNode.FALSE, nnf);
    }

    public void testNnfAbsence2() throws LtlParseException {
        LtlNode t = parser.parse("!wasEvent(p1.e1)");
        LtlNode nnf = LtlUtils.getInstance().toNnf(t);
        assertEquals(t, nnf);
        assertTrue(UnaryOperator.class.isAssignableFrom(nnf.getClass()));
        UnaryOperator op = (UnaryOperator) nnf;
        assertEquals(UnaryOperatorType.NEG, op.getType());
        assertEquals("wasEvent", op.getOperand().getName());
    }

    public void testNnfBoolean() throws LtlParseException {
        LtlNode t = parser.parse("!false");
        t = LtlUtils.getInstance().toNnf(t);
        assertEquals(BooleanNode.TRUE, t);
    }

    public void testNext() throws LtlParseException {
        LtlNode t = parser.parse("!X(false)");
        t = LtlUtils.getInstance().toNnf(t);
        assertTrue(UnaryOperator.class.isAssignableFrom(t.getClass()));
        UnaryOperator op = (UnaryOperator) t;
        assertEquals(UnaryOperatorType.NEXT, op.getType());
        assertEquals(BooleanNode.TRUE, op.getOperand());
    }

    public void testNnfUntil() throws LtlParseException {
        binaryOperatorTest("!U(false, wasEvent(p1.e1))", BinaryOperatorType.RELEASE);
    }

    public void testNnfRelease() throws LtlParseException {
        binaryOperatorTest("!R(false, wasEvent(p1.e1))", BinaryOperatorType.UNTIL);
    }

    public void testNnfAnd() throws LtlParseException {
        binaryOperatorTest("!(false && wasEvent(p1.e1))", BinaryOperatorType.OR);
    }

    public void testNnfOr() throws LtlParseException {
        binaryOperatorTest("!(false || wasEvent(p1.e1))", BinaryOperatorType.AND);
    }

    private void binaryOperatorTest(String expr, BinaryOperatorType expected) throws LtlParseException {
        LtlNode t = parser.parse(expr);
        t = LtlUtils.getInstance().toNnf(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(expected, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertTrue(UnaryOperator.class.isAssignableFrom(op.getRightOperand().getClass()));
        UnaryOperator unOp = (UnaryOperator) op.getRightOperand();
        assertEquals(UnaryOperatorType.NEG, unOp.getType());
        assertEquals("wasEvent", unOp.getOperand().getName());
    }

    public void testNnfNeg1() throws LtlParseException {
        LtlNode t = parser.parse("!!wasEvent(p1.e1)");
        t = LtlUtils.getInstance().toNnf(t);
        assertEquals("wasEvent", t.getName());
    }

    public void testNnfNeg2() throws LtlParseException {
        LtlNode t = parser.parse("!!true");
        t = LtlUtils.getInstance().toNnf(t);
        assertEquals(BooleanNode.TRUE, t);
    }

    public void testNnfUntil2() throws LtlParseException {
        LtlNode t = parser.parse("U(!true, !X(wasEvent(p1.e1)))");
        t = LtlUtils.getInstance().toNnf(t);
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.FALSE, op.getLeftOperand());
        assertTrue(UnaryOperator.class.isAssignableFrom(op.getRightOperand().getClass()));
        UnaryOperator unOp = (UnaryOperator) op.getRightOperand();
        assertEquals(UnaryOperatorType.NEXT, unOp.getType());
        assertTrue(UnaryOperator.class.isAssignableFrom(unOp.getOperand().getClass()));
        unOp = (UnaryOperator) unOp.getOperand();
        assertEquals(UnaryOperatorType.NEG, unOp.getType());
        assertEquals("wasEvent", unOp.getOperand().getName());
    }

}
