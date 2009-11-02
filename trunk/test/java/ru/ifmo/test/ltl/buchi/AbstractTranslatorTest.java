/*
 * Developed by eVelopers Corporation - 26.05.2008
 */
package ru.ifmo.test.ltl.buchi;

import junit.framework.TestCase;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.io.StateMachineReader;

import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;

public abstract class AbstractTranslatorTest extends TestCase {
    protected ILtlParser parser;

    protected void setUp() throws IOException, AutomataFormatException {
        SimplePredicateFactory predicates = new SimplePredicateFactory();
        IAutomataContext context = new AutomataContext(new StateMachineReader("CarA1.xml"));
        parser = new LtlParser(context, predicates);
    }

    protected abstract ITranslator getTranslator();

    protected IBuchiAutomata extractBuchi(String expr) throws LtlParseException {
        LtlNode t = parser.parse(expr);
        ITranslator translator = getTranslator();
        return translator.translate(t);
    }

    private class SimplePredicateFactory implements IPredicateFactory<IState> {
        @Predicate
        public boolean p1() {
            return true;
        }

        @Predicate
        public boolean p2() {
            return false;
        }

        @Predicate
        public boolean p3() {
            return true;
        }

        @Predicate
        public boolean p4() {
            return true;
        }

        public void setAutomataState(IState state, IStateTransition transition) {
            throw new NotImplementedException();
        }

        @Predicate
        public Boolean wasEvent(IEvent e) {
            throw new NotImplementedException();
        }

        public Boolean isInState(IStateMachine<? extends IState> a, IState s) {
            throw new NotImplementedException();
        }

        @Predicate
        public Boolean wasInState(IStateMachine<? extends IState> a, IState s) {
            throw new NotImplementedException();
        }

        @Predicate
        public boolean cameToFinalState() {
            throw new NotImplementedException();
        }

        @Predicate
        public Boolean wasAction(IAction z) {
            throw new NotImplementedException();
        }

        @Predicate
        public Boolean wasFirstAction(IAction z) {
            throw new NotImplementedException();
        }

        @Predicate
        public boolean wasTrue(ICondition cond) {
            throw new NotImplementedException();
        }

        @Predicate
        public boolean wasFalse(ICondition cond) {
            throw new NotImplementedException();
        }
    }
}
