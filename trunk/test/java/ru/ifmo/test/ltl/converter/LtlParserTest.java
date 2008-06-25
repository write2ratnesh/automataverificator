/**
 * GrammarConverterTest.java, 12.03.2008
 */
package ru.ifmo.test.ltl.converter;

import junit.framework.TestCase;
import ru.ifmo.ltl.grammar.*;
import ru.ifmo.ltl.grammar.predicate.PredicateFactory;
import ru.ifmo.ltl.grammar.exception.UnexpectedOperatorException;
import ru.ifmo.ltl.grammar.exception.UnexpectedMethodException;
import ru.ifmo.ltl.grammar.exception.UnexpectedParameterException;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.io.UnimodXmlReader;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.IAutomataContext;

import java.io.IOException;

/**
 * Test LtlParser
 *
 * @author: Kirill Egorov
 */
public class LtlParserTest extends TestCase {

    private ILtlParser parser;

    protected void setUp() throws IOException, AutomataFormatException {
        PredicateFactory predicates = new PredicateFactory();
        IAutomataContext context = new AutomataContext(new UnimodXmlReader("CarA1.xml"));
        parser = new LtlParser(context, predicates);
    }

    public void testConverterUntil() throws LtlParseException {
        LtlNode t = parser.parse(" U(isInState(A1, A1[\"create new\"]), wasInState(A1, A1.crash))");

        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertEquals("wasInState", op.getRightOperand().getName());
    }

    public void testConverterFuture() throws LtlParseException {
        LtlNode t = parser.parse("F(isInState(A1, A1.s1) && wasInState(A1, A1[\"s1\"]))");

        assertTrue(UnaryOperator.class.isAssignableFrom(t.getClass()));
        UnaryOperator op1 = (UnaryOperator) t;
        assertEquals(UnaryOperatorType.FUTURE, op1.getType());
        t = op1.getOperand();
        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op2 = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.AND, op2.getType());
        assertEquals("isInState", op2.getLeftOperand().getName());
        assertEquals("wasInState", op2.getRightOperand().getName());
    }

    public void testConverterOr1() throws LtlParseException {
        LtlNode t = parser.parse("isInState(A1, A1.s1) || wasInState(A1, A1.s1)");

        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.OR, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertEquals("wasInState", op.getRightOperand().getName());
    }

    public void testConverterOr2() throws LtlParseException {
        LtlNode t = parser.parse("isInState(A1, A1.s1) || wasInState(A1, A1.s1) || wasEvent(p1.e1)");

        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.OR, op.getType());
        assertEquals("isInState", op.getLeftOperand().getName());
        assertTrue(BinaryOperator.class.isAssignableFrom(op.getRightOperand().getClass()));
        op = (BinaryOperator) op.getRightOperand();
        assertEquals("wasInState", op.getLeftOperand().getName());
        assertEquals("wasEvent", op.getRightOperand().getName());
    }

    public void testConverterFutureFail() throws LtlParseException {
        try {
            parser.parse("F(isInState(A1, A1.s1), wasInState(A1, A1.s1))");
            fail();
        } catch (UnexpectedOperatorException e) {
            //
        }
    }

    public void testConverterUntilFail() throws LtlParseException {
        try {
            parser.parse("U(isInState(A1, A1.s1))");
            fail();
        } catch (UnexpectedOperatorException e) {
            //
        }
    }

    public void testConverterWrongOperator() throws LtlParseException {
        try {
            parser.parse("N(isInState(A1, A1.s1))");
            fail();
        } catch (UnexpectedMethodException e) {
            //
        }
    }

    public void testConverterBoolean1() throws LtlParseException {
        LtlNode t = parser.parse("U(true, isInState(A1, A1.s1))");

        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.UNTIL, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertEquals("true", op.getLeftOperand().getName());
        assertEquals("isInState", op.getRightOperand().getName());
    }

    public void testConverterBoolean2() throws LtlParseException {
        LtlNode t = parser.parse("true && X(isInState(A1, A1.s1))");

        assertTrue(BinaryOperator.class.isAssignableFrom(t.getClass()));
        BinaryOperator op = (BinaryOperator) t;
        assertEquals(BinaryOperatorType.AND, op.getType());
        assertEquals(BooleanNode.TRUE, op.getLeftOperand());
        assertEquals("true", op.getLeftOperand().getName());
        UnaryOperator unOp = (UnaryOperator) op.getRightOperand();
        assertEquals("X", unOp.getName());
        assertEquals("isInState", unOp.getOperand().getName());
    }

    //Predicate tests

    public void testPredicateWithoutArgs() throws LtlParseException {
        LtlNode node = parser.parse("cameToFinalState()");

        assertTrue(Predicate.class.isAssignableFrom(node.getClass()));
        Predicate p = (Predicate) node;
        assertEquals("cameToFinalState", p.getName());
    }

    public void testPredicateWithArgs() throws LtlParseException {
        LtlNode node = parser.parse("wasInState(A1, A1.s1)");

        assertTrue(Predicate.class.isAssignableFrom(node.getClass()));
        Predicate p = (Predicate) node;
        assertEquals("wasInState", p.getName());
    }

    public void testPredicateWithNullArg() throws LtlParseException {
        LtlNode node = parser.parse("wasInState(null, A1.s1)");

        assertTrue(Predicate.class.isAssignableFrom(node.getClass()));
        Predicate p = (Predicate) node;
        assertEquals("wasInState", p.getName());
    }

    public void testPredicateWithWrongArg() throws LtlParseException {
        try {
            parser.parse("wasInState(1, A1.s1)");
            fail();
        } catch (UnexpectedParameterException e) {
            //do nothing
        }
    }

    public void testPredicateWithWrongArg2() throws LtlParseException {
        try {
            parser.parse("wasInState(s1, A1.s1)");
            fail();
        } catch (UnexpectedParameterException e) {
            //do nothing
        }
    }
}
