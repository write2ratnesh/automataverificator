/**
 * UnimodXmlReaderTest.java, 02.03.2008
 */
package ru.ifmo.test.automata.statemashine;

import junit.framework.TestCase;
import ru.ifmo.automata.statemashine.impl.UnimodXmlReader;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.StateType;

import java.io.IOException;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

/**
 * Test unimod model reader
 *
 * @author: Kirill Egorov
 */
public class UnimodXmlReaderTest extends TestCase {

    public void testReadRootStateMashine() throws IOException, AutomataFormatException {
        IAutomataReader reader = new UnimodXmlReader("CarA1.xml");
        IStateMashine<? extends IState> m = reader.readRootStateMashine();
        assertEquals(6, m.getStates().size());
        assertNull(m.getState("Top"));
        assertEquals(StateType.INITIAL, m.getState("s1").getType());
        assertEquals(StateType.NORMAL, m.getState("leave bounds").getType());
        assertEquals(3, m.getState("do maneuver").getActions().size());
        assertEquals(6, m.getState("move to player").getOutcomingTransitions().size());
        assertEquals("A1", m.getName());
    }

    public void testReadCtrlObjects() throws IOException, AutomataFormatException {
        IAutomataReader reader = new UnimodXmlReader("CarA1.xml");
        Map<String, IControlledObject> ctrls = reader.readControlledObjects();

        assertTrue(ctrls.containsKey("o1"));
        IControlledObject o = ctrls.get("o1");
        Collection<IAction> actions = o.getActions();
        assertEquals(13, actions.size());
    }

    public void testReadEventProviders() throws IOException, AutomataFormatException {
        IAutomataReader reader = new UnimodXmlReader("CarA1.xml");


        Map<String, ? extends IStateMashine<? extends IState>> stateMashines = reader.readStateMashines();
        Set<IEventProvider> eventProviders = stateMashines.get("A1").getEventProviders();
        assertEquals(1, eventProviders.size());
        
        IEventProvider p = eventProviders.iterator().next();
        assertEquals("p1", p.getName());

        assertEquals(6, p.getEvents().size());
    }
}