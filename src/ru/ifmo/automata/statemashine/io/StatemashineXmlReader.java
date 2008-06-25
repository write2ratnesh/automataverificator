/*
 * Developed by eVelopers Corporation - 25.06.2008
 */
package ru.ifmo.automata.statemashine.io;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.impl.*;
import ru.ifmo.automata.statemashine.io.IAutomataReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLClassLoader;
import java.net.URL;

public class StatemashineXmlReader implements IAutomataReader {

    private static final String STATE_MASHINE = "stateMachine";
    private static final String ROOT_STATE_MASHINE = "rootStateMachine";
    private static final String CTRL_OBJECT = "controlledObject";
    private static final String EVENT_PROVIDER = "eventProvider";
    private static final String ASSOCIATION = "association";
    private static final String STATE = "state";
    private static final String OUT_ACTION = "outputAction";
    private static final String TRANSITION = "transition";
    private static final String STATE_MASHINE_REF = "stateMachineRef";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_CLASS = "class";
    private static final String ATTR_TARGET = "targetRef";
    private static final String ATTR_SOURCE = "sourceRef";
    private static final String ATTR_ACTION = "ident";
    private static final String ATTR_EVENT = "event";
    private static final String ATTR_COND = "guard";
    private static final String ATTR_SUPPLIER_ROLE = "supplierRole";
    private static final String ANY_EVENT = "*";

    private StateMashine<State> rootStateMashine;
    private Map<String, StateMashine<State>> stateMashines;
    private Map<String, IEventProvider> eventProviders;
    private Map<String, IControlledObject> ctrlObjects;

    private XMLStreamReader reader;

    public StatemashineXmlReader(String fileLocation) throws IOException {
        URLClassLoader urlLoader = (URLClassLoader) getClass().getClassLoader();
        URL fileLoc = urlLoader.findResource(fileLocation);

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

            reader = inputFactory.createXMLStreamReader(fileLoc.openStream());
        } catch (XMLStreamException e) {
            throw new IOException("Cant parse " + fileLocation, e);
        }
    }

    public StatemashineXmlReader(File file) throws IOException {
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
                    if (ASSOCIATION.equals(elementName)) {
                        parseCtrlObjAssociation(sm);
                    } else if (STATE.equals(elementName)) {
                        parseStates(sm);
                    } else if (TRANSITION.equals(elementName)) {
                        parseTransitions(sm);
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
                    //TODO: parse single state
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    startCount--;
                default:
                    break;
            }
        }
        if (!STATE.equals(reader.getName().toString())) {
            throw new AutomataFormatException("Unexpected end tag: " + reader.getName());
        }
    }

    private void parseTransitions(StateMashine<State> sm) {
        //TODO
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
