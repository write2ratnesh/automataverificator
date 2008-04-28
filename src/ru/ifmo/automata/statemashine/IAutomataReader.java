/**
 * IAutomataReader.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine;

import ru.ifmo.automata.statemashine.impl.AutomataFormatException;

import java.util.Map;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public interface IAutomataReader {

    public IStateMashine<? extends IState> readRootStateMashine() throws AutomataFormatException;
    public Map<String, IEventProvider> readEventProviders() throws AutomataFormatException;
    public Map<String, IControlledObject> readControlledObjects() throws AutomataFormatException;
    public Map<String, ? extends IStateMashine<? extends IState>> readStateMashines() throws AutomataFormatException;
}
