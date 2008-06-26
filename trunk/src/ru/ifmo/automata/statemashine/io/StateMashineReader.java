/*
 * Developed by eVelopers Corporation - 25.06.2008
 */
package ru.ifmo.automata.statemashine.io;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.impl.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLClassLoader;
import java.net.URL;

import static ru.ifmo.automata.statemashine.StateMashineUtils.*;

public class StateMashineReader implements IAutomataReader {

    private StateMashine<State> rootStateMashine;
    private Map<String, StateMashine<State>> stateMashines;
    private Map<String, IEventProvider> eventProviders;
    private Map<String, IControlledObject> ctrlObjects;

    private XMLStreamReader reader;

    public StateMashineReader(String fileLocation) throws IOException {
        URLClassLoader urlLoader = (URLClassLoader) getClass().getClassLoader();
        URL fileLoc = urlLoader.findResource(fileLocation);

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

            reader = inputFactory.createXMLStreamReader(fileLoc.openStream());
        } catch (XMLStreamException e) {
            throw new IOException("Can't parse " + fileLocation, e);
        }
    }

    public StateMashineReader(File file) throws IOException {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

            reader = inputFactory.createXMLStreamReader(new FileInputStream(file));
        } catch (XMLStreamException e) {
            throw new IOException("Cant parse " + file.getPath(), e);
        }
    }

    public void close() throws IOException {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void init() {
        if (rootStateMashine == null) {
            ctrlObjects = new HashMap<String, IControlledObject>();
            eventProviders = new HashMap<String, IEventProvider>();
            stateMashines = new HashMap<String, StateMashine<State>>();
        }
    }

    public IStateMashine<? extends IState> readRootStateMashine() throws AutomataFormatException {
        try {
            init();
            readAll();
            return rootStateMashine;
        } catch (XMLStreamException e) {
            throw new AutomataFormatException(e);
        } catch (ClassNotFoundException e) {
            throw new AutomataFormatException(e);
        }
    }

    public Map<String, IEventProvider> readEventProviders() throws AutomataFormatException {
        try {
            init();
            readAll();
            return eventProviders;
        } catch (XMLStreamException e) {
            throw new AutomataFormatException(e);
        } catch (ClassNotFoundException e) {
            throw new AutomataFormatException(e);
        }
    }

    public Map<String, IControlledObject> readControlledObjects() throws AutomataFormatException {
        try {
            init();
            readAll();
            return ctrlObjects;
        } catch (XMLStreamException e) {
            throw new AutomataFormatException(e);
        } catch (ClassNotFoundException e) {
            throw new AutomataFormatException(e);
        }
    }

    public Map<String, ? extends IStateMashine<? extends IState>> readStateMashines() throws AutomataFormatException {
        try {
            init();
            readAll();
            return stateMashines;
        } catch (XMLStreamException e) {
            throw new AutomataFormatException(e);
        } catch (ClassNotFoundException e) {
            throw new AutomataFormatException(e);
        }
    }

    protected void readAll() throws XMLStreamException, ClassNotFoundException, AutomataFormatException {
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = reader.getName().toString();
                    if (CTRL_OBJECT.equals(elementName)) {
                        parseControlledObject();
                    } else if (EVENT_PROVIDER.equals(elementName)) {
                        parseEventProvider();
                    } else if (STATE_MASHINE_REF.equals(elementName)) {
                        parseRootStateMashine();
                    } else if (STATE_MASHINE.equals(elementName)) {
                        parseStateMashine();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void parseRootStateMashine() {
        assert STATE_MASHINE_REF.equals(reader.getName().toString());

        String name = reader.getAttributeValue(null, ATTR_NAME);
        rootStateMashine = getStateMashine(name);
    }

    private void parseStateMashine() throws XMLStreamException, AutomataFormatException {
        assert STATE_MASHINE.equals(reader.getName().toString());

        String name = reader.getAttributeValue(null, ATTR_NAME);
        StateMashine<State> sm = getStateMashine(name);

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = reader.getName().toString();
                    if (TRANSITION.equals(elementName)) {
                        parseTransition(sm);
                    } else if (ASSOCIATION.equals(elementName)) {
                        parseCtrlObjAssociation(sm);
                    } else if (STATE.equals(elementName)) {
                        parseStates(sm);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void parseControlledObject() throws ClassNotFoundException {
        assert CTRL_OBJECT.equals(reader.getName().toString());

        String name = reader.getAttributeValue(null, ATTR_NAME);
        String className = reader.getAttributeValue(null, ATTR_CLASS);
        Class aClass = Class.forName(className);
        ctrlObjects.put(name, new ControlledObject(name, aClass));
    }

    private void parseEventProvider() throws ClassNotFoundException, XMLStreamException, AutomataFormatException {
        assert EVENT_PROVIDER.equals(reader.getName().toString());

        String name = reader.getAttributeValue(null, ATTR_NAME);
        String className = reader.getAttributeValue(null, ATTR_CLASS);
        Class aClass = Class.forName(className);
        EventProvider eProvider = new EventProvider(name, aClass);
        eventProviders.put(name, eProvider);

        int startCount = 1;
        while (startCount > 0) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    startCount++;
                    if (ASSOCIATION.equals(reader.getName().toString())) {
                        String smName = reader.getAttributeValue(null, ATTR_TARGET);

                        getStateMashine(smName).addEventProvider(eProvider);
                    } else {
                        throw new AutomataFormatException("Unexpected element name: " + reader.getName());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    startCount--;
                    break;
                default:
                    break;
            }
        }
        if (!EVENT_PROVIDER.equals(reader.getName().toString())) {
            throw new AutomataFormatException("Unexpected end tag: " + reader.getName());
        }
    }

    private void parseStates(StateMashine<State> sm) throws XMLStreamException, AutomataFormatException {
        assert STATE.equals(reader.getName().toString());
        assert "Top".equals(reader.getAttributeValue(null, ATTR_NAME));

        int startCount = 1;
        while (startCount > 0) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    startCount++;
                    if (STATE.equals(reader.getName().toString())) {
                        parseSingleState(sm);
                    } else {
                        throw new AutomataFormatException("Unexpected tag: " + reader.getName());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    startCount--;
                    break;
                default:
                    break;
            }
        }
        if (!STATE.equals(reader.getName().toString())) {
            throw new AutomataFormatException("Unexpected end tag: " + reader.getName());
        }
    }

    private void parseSingleState(StateMashine<State> sm) throws XMLStreamException, AutomataFormatException {
        assert STATE.equals(reader.getName().toString());

        String name = reader.getAttributeValue(null, ATTR_NAME);
        String type = reader.getAttributeValue(null, ATTR_TYPE);
        List<IAction> actions = new ArrayList<IAction>();
        State state = new State(name, StateType.getByName(type), actions);

        int startCount = 1;
        while (startCount > 0) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    startCount++;
                    String elementName = reader.getName().toString();
                    if (OUT_ACTION.equals(elementName)) {
                        String actionFullName = reader.getAttributeValue(null, ATTR_ACTION);
                        actions.add(StateMashineUtils.extractAction(actionFullName, sm));
                    } else if (STATE_MASHINE_REF.equals(elementName)) {
                        String smName = reader.getAttributeValue(null, ATTR_NAME);
                        StateMashine<State> nested = stateMashines.get(smName);
                        state.addNestedStateMashine(nested);
                        nested.setParent(sm, state);
                        sm.addNestedStateMashine(nested);
                    } else {
                        throw new AutomataFormatException("Unexpected tag: " + reader.getName());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    startCount--;
                    break;
                default:
                    break;
            }
        }

        sm.addState(state);

        if (!STATE.equals(reader.getName().toString())) {
            throw new AutomataFormatException("Unexpected end tag: " + reader.getName());
        }
    }

    private void parseTransition(StateMashine<State> sm) throws XMLStreamException, AutomataFormatException {
        assert TRANSITION.equals(reader.getName().toString());

        String eventFullName = reader.getAttributeValue(null, ATTR_EVENT);
        String condExpr      = reader.getAttributeValue(null, ATTR_COND);
        String source        = reader.getAttributeValue(null, ATTR_SOURCE);
        String target        = reader.getAttributeValue(null, ATTR_TARGET);

        State stateSource = sm.getState(source);
        State stateTarget = sm.getState(target);

        IEvent event = StateMashineUtils.parseEvent(sm, eventProviders, eventFullName);
        Transition t = new Transition(event, new Condition(condExpr), stateTarget);

        int startCount = 1;
        while (startCount > 0) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    startCount++;
                    if (OUT_ACTION.equals(reader.getName().toString())) {
                        String actionFullName = reader.getAttributeValue(null, ATTR_ACTION);
                        t.addAction(StateMashineUtils.extractAction(actionFullName, sm));
                    } else {
                        throw new AutomataFormatException("Unexpected tag: " + reader.getName());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    startCount--;
                    break;
                default:
                    break;
            }
        }
        stateSource.addOutcomingTransition(t);

        if (!TRANSITION.equals(reader.getName().toString())) {
            throw new AutomataFormatException("Unexpected end tag: " + reader.getName());
        }
    }

    private void parseCtrlObjAssociation(StateMashine<State> sm) {
        String role = reader.getAttributeValue(null, ATTR_SUPPLIER_ROLE);
        String target = reader.getAttributeValue(null, ATTR_TARGET);

        IControlledObject ctrl = ctrlObjects.get(target);
        if (ctrl != null) {
            sm.addControlledObject(role, ctrl);
        }
    }

    protected StateMashine<State> getStateMashine(String name) {
        StateMashine<State> sm = stateMashines.get(name);
        if (sm == null) {
            sm = new StateMashine<State>(name);
            stateMashines.put(name, sm);
        }
        return sm;
    }
}