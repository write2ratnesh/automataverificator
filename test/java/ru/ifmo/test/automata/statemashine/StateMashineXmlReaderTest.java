/*
 * Developed by eVelopers Corporation - 25.06.2008
 */
package ru.ifmo.test.automata.statemashine;

import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.io.IAutomataReader;
import ru.ifmo.automata.statemashine.io.StateMashineXmlReader;
import ru.ifmo.automata.statemashine.*;

import java.io.IOException;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import junit.framework.TestCase;

public class StateMashineXmlReaderTest extends TestCase {
    public void testReadRootStateMashine() throws IOException, AutomataFormatException {
        IAutomataReader reader = new StateMashineXmlReader("CarA1.xml");
        try {
            IStateMashine<? extends IState> m = reader.readRootStateMashine();
            assertEquals(6, m.getStates().size());
            assertNull(m.getState("Top"));
            assertEquals(StateType.INITIAL, m.getState("s1").getType());
            assertEquals(StateType.NORMAL, m.getState("leave bounds").getType());
            assertEquals(3, m.getState("do maneuver").getActions().size());
            assertEquals(6, m.getState("move to player").getOutcomingTransitions().size());
            assertEquals("A1", m.getName());
        } finally {
            reader.close();
        }
    }

    public void testReadCtrlObjects() throws IOException, AutomataFormatException {
        IAutomataReader reader = new StateMashineXmlReader("CarA1.xml");
        try {
            Map<String, IControlledObject> ctrls = reader.readControlledObjects();

            assertTrue(ctrls.containsKey("o1"));
            IControlledObject o = ctrls.get("o1");
            Collection<IAction> actions = o.getActions();
            assertEquals(13, actions.size());
        } finally {
            reader.close();
        }
    }

    public void testReadEventProviders1() throws IOException, AutomataFormatException {
        IAutomataReader reader = new StateMashineXmlReader("CarA1.xml");


        Map<String, ? extends IStateMashine<? extends IState>> stateMashines = reader.readStateMashines();
        Set<IEventProvider> eventProviders = stateMashines.get("A1").getEventProviders();
        assertEquals(1, eventProviders.size());

        IEventProvider p = eventProviders.iterator().next();
        assertEquals("p1", p.getName());

        assertEquals(6, p.getEvents().size());
    }

    public void testReadEventProviders2() throws IOException, AutomataFormatException {
        IAutomataReader reader = new StateMashineXmlReader("GameA1.xml");


        Map<String, ? extends IStateMashine<? extends IState>> stateMashines = reader.readStateMashines();
        Set<IEventProvider> eventProviders = stateMashines.get("A1").getEventProviders();
        assertEquals(3, eventProviders.size());
    }
}