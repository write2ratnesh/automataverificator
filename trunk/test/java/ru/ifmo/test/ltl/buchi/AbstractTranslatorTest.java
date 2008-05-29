/*
 * Developed by eVelopers Corporation - 26.05.2008
 */
package ru.ifmo.test.ltl.buchi;

import junit.framework.TestCase;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.grammar.predicate.annotation.Predicate;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.impl.UnimodXmlReader;
import ru.ifmo.automata.statemashine.*;

import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;

public abstract class AbstractTranslatorTest extends TestCase {
    protected ILtlParser parser;

    protected void setUp() throws IOException, AutomataFormatException {
        SimplePredicateFactory predicates = new SimplePredicateFactory();
        IAutomataContext context = new AutomataContext(new UnimodXmlReader("CarA1.xml"));
        parser = new LtlParser(context, predicates);
    }

    protected abstract IBuchiAutomata extractBuchi(String expr) throws LtlParseException;

    private class SimplePredicateFactory implements IPredicateFactory<IState> {
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

        @Predicate
        public Boolean wasEvent(IEvent e) {
            throw new NotImplementedException();
        }

        public Boolean isInState(IStateMashine<? extends IState> a, IState s) {
            throw new NotImplementedException();
        }

        @Predicate
        public Boolean wasInState(IStateMashine<? extends IState> a, IState s) {
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
