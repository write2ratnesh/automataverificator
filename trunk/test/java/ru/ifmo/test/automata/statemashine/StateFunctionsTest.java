/**
 * StateFunctionsTest.java, 05.03.2008
 */
package ru.ifmo.test.automata.statemashine;

import junit.framework.TestCase;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.StateMachine;
import ru.ifmo.automata.statemashine.io.IAutomataReader;
import ru.ifmo.automata.statemashine.io.StateMachineReader;
import ru.ifmo.automata.statemashine.IControlledObject;
import ru.ifmo.automata.statemashine.IFunction;

import java.util.List;
import java.io.IOException;

/**
 * test function state's list
 *
 * @author Kirill Egorov
 */
public class StateFunctionsTest extends TestCase {

    private StateMachine m;

    protected void setUp() throws IOException, AutomataFormatException {
        IAutomataReader reader = new StateMachineReader("CarA1.xml");
        m = (StateMachine) reader.readRootStateMachine();
        reader.close();
    }

    public void testGetStateFunctions1() throws AutomataFormatException, IOException {
        List<IFunction> funList = m.getStateFunctions(m.getState("do maneuver"));
        assertEquals(3, funList.size());
        IControlledObject o = m.getControlledObject("o1");
        assertTrue(funList.contains(o.getFunction("x2")));
        assertTrue(funList.contains(o.getFunction("x3")));
        assertTrue(funList.contains(o.getFunction("x4")));
    }

    public void testGetStateFunctions2() throws AutomataFormatException, IOException {
        List<IFunction> funList = m.getStateFunctions(m.getState("move to player"));
        assertEquals(3, funList.size());
        IControlledObject o = m.getControlledObject("o1");
        assertTrue(funList.contains(o.getFunction("x1")));
        assertTrue(funList.contains(o.getFunction("x2")));
        assertTrue(funList.contains(o.getFunction("x3")));
    }

    public void testGetStateFunctions3() throws AutomataFormatException, IOException {
        List<IFunction> funList = m.getStateFunctions(m.getState("leave bounds"));
        assertTrue(funList.isEmpty());
    }
}
