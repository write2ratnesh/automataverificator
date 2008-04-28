/**
 * TranslatorTest.java, 23.03.2008
 */
package ru.ifmo.test.ltl.buchi;

import junit.framework.TestCase;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.ltl.buchi.ITransitionCondition;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.impl.UnimodXmlReader;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateTransition;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

/**
 * Test translatin from ltl to buchi
 *
 * @author: Kirill Egorov
 */
public class TranslatorTest extends TestCase {

    private ILtlParser parser;

    protected void setUp() throws IOException, AutomataFormatException {
        SimplePredicateUtils predicates = new SimplePredicateUtils();
        IAutomataContext context = new AutomataContext(new UnimodXmlReader("CarA1.xml"));
        parser = new LtlParser(context, predicates);
    }


    public void testTranslationUntil() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi(" U(p1(), p2())");

        assertEquals(4, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(2, transitons.size());
        System.out.print(buchi);
    }

    public void testTranslationFuture() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi("F(p1())");

        assertEquals(4, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(2, transitons.size());
        System.out.print(buchi);
    }

    public void testTranslationRelease() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi(" R(p1(), p2())");
        assertEquals(4, buchi.size());
        System.out.println(buchi);
    }

    public void testTranslationGlobal() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi(" G(p1())");
        assertEquals(2, buchi.size());
        System.out.println(buchi);
    }

    public void testNext() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi("X(p1())");

        assertEquals(4, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(1, transitons.size());
        System.out.print(buchi);
    }

    public void testNextNext() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi("X(X(p1()))");

        assertEquals(5, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(1, transitons.size());
        System.out.print(buchi);
    }

    public void testNegNext() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi("!(X(p1()))");

        assertEquals(4, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(1, transitons.size());
        System.out.print(buchi);
    }

    public void testUntilAndUntil() throws LtlParseException {
        IBuchiAutomata buchi = extractBuchi("U(p1(), p2()) && U(p1(), p2())");
        System.out.print(buchi);

        assertEquals(14, buchi.size());
        IBuchiNode start = buchi.getStartNode();
        Map<ITransitionCondition, IBuchiNode> transitons = start.getTransitions();
        assertEquals(4, transitons.size());
    }
    
    private IBuchiAutomata extractBuchi(String expr) throws LtlParseException {
        LtlNode t = parser.parse(expr);
        ITranslator translator = new SimpleTranslator();
        return translator.translate(t);
    }

    private class SimplePredicateUtils implements IPredicateUtils {
        @Predicate
        public boolean p1() {
            return true;
        }

        @Predicate
        public boolean p2() {
            return false;
        }

        public void setAutomataState(IState state, IStateTransition transition) {
            throw new NotImplementedException();
        }
    }
}
