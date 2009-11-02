/**
 * IAutomataReader.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine.io;

import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.IStateMachine;
import ru.ifmo.automata.statemashine.IEventProvider;
import ru.ifmo.automata.statemashine.IControlledObject;
import ru.ifmo.automata.statemashine.IState;

import java.util.Map;
import java.io.Closeable;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface IAutomataReader extends Closeable {

    IStateMachine<? extends IState> readRootStateMachine() throws AutomataFormatException;
    Map<String, IEventProvider> readEventProviders() throws AutomataFormatException;
    Map<String, IControlledObject> readControlledObjects() throws AutomataFormatException;
    Map<String, ? extends IStateMachine<? extends IState>> readStateMachines() throws AutomataFormatException;
}
