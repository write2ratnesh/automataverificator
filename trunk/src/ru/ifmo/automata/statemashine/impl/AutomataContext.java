/**
 * ${NAME}.java, 13.03.2008
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.io.IAutomataReader;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class AutomataContext implements IAutomataContext {

    private Map<String, IControlledObject> ctrlObjects = new HashMap<String, IControlledObject>();
    private Map<String, IEventProvider> eventProviders = new HashMap<String, IEventProvider>();
    private Map<String, IStateMashine<? extends IState>> stateMashines
            = new HashMap<String, IStateMashine<? extends IState>>();

    public AutomataContext() {
    }

    /**
     * Create new automata context instance and close <code>reader</code>.
     * @param reader
     * @throws AutomataFormatException
     * @throws IOException
     */
    public AutomataContext(IAutomataReader reader) throws AutomataFormatException, IOException {
        try {
            putAll(reader);
        } catch (AutomataFormatException e) {
            reader.close();
        }

    }

    public IControlledObject getControlledObject(String name) {
        return ctrlObjects.get(name);
    }

    public IEventProvider getEventProvider(String name) {
        return eventProviders.get(name);
    }

    public IStateMashine<? extends IState> getStateMashine(String name) {
        return stateMashines.get(name);
    }

    public void putControlledObject(String name, IControlledObject o) {
        ctrlObjects.put(name, o);
    }

    public void putEventProvider(String name, IEventProvider p) {
        eventProviders.put(name, p);
    }

    public void putStateMashine(String name, IStateMashine<? extends IState> m) {
        stateMashines.put(name, m);
    }

    public void putAll(IAutomataReader reader) throws AutomataFormatException {
        ctrlObjects.putAll(reader.readControlledObjects());
        stateMashines.putAll(reader.readStateMashines());
        eventProviders.putAll(reader.readEventProviders());
    }
}
