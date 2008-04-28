/**
 * UnimodXmlReader.java, 02.03.2008
 */
package ru.ifmo.automata.statemashine.impl;

import ru.ifmo.automata.statemashine.*;
import ru.ifmo.automata.statemashine.IEvent;
import ru.ifmo.automata.statemashine.StateType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.IOException;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.apache.commons.lang.StringUtils;

/**
 * Xml unimod model reader
 *
 * @author: Kirill Egorov
 */
public class UnimodXmlReader implements IAutomataReader {

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

    private Document document;

    private Map<String, StateMashine<State>> stateMashines;
    private Map<String, IEventProvider> eventProviders;
    private Map<String, IControlledObject> ctrlObjects;

    public UnimodXmlReader(String fileLocation) throws IOException {
        try {
            DocumentBuilder builder = createDocumentBuider();
            URLClassLoader urlLoader = (URLClassLoader) getClass().getClassLoader();
            URL fileLoc = urlLoader.findResource(fileLocation);
            document = builder.parse(fileLoc.openStream());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new IOException("Cant parse " + fileLocation, e);
        }

    }

    public UnimodXmlReader(File file) throws IOException {
        try {
            DocumentBuilder builder = createDocumentBuider();
            document = builder.parse(file);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new IOException("Cant parse " + file.getPath(), e);
        }
    }

    private DocumentBuilder createDocumentBuider() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return factory.newDocumentBuilder();
    }

    public IStateMashine<? extends IState> readRootStateMashine() throws AutomataFormatException {
        readStateMashines();
        
        NodeList l1 = document.getElementsByTagName(ROOT_STATE_MASHINE);
        Element e = (Element) l1.item(0);
        NodeList l2 = e.getElementsByTagName(STATE_MASHINE_REF);
        Element root = (Element) l2.item(0);
        String name = root.getAttribute(ATTR_NAME);
        return stateMashines.get(name);
    }

    public Map<String, IEventProvider> readEventProviders() throws AutomataFormatException {
        if (eventProviders != null) {
            return eventProviders;
        }
        eventProviders = new HashMap<String, IEventProvider>();
        createStateMashines();

        try {
            NodeList list = document.getElementsByTagName(EVENT_PROVIDER);
            if (list == null) {
                return null;
            }
            for (int i = 0; i < list.getLength(); i++) {
                //parse event provider
                NamedNodeMap attrs = list.item(i).getAttributes();
                String name = attrs.getNamedItem(ATTR_NAME).getNodeValue();
                String className = attrs.getNamedItem(ATTR_CLASS).getNodeValue();
                Class aClass = Class.forName(className);
                IEventProvider provider = new EventProvider(name, aClass);
                eventProviders.put(name, provider);

                //parse event provider targets (state mashines to be notified)
                Collection<StateMashine<State>> targets = parseTargets((Element) list.item(i));
                for (StateMashine<State> m: targets) {
                    m.addEventProvider(provider);
                }
            }
            return eventProviders;
        } catch (ClassNotFoundException e) {
            eventProviders = null;
            throw new AutomataFormatException("Event provider has unknown class", e);
        }

    }

    private Collection<StateMashine<State>> parseTargets(Element e) {
        if (!StringUtils.equals(EVENT_PROVIDER, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't eventProvider");
        }
        Collection<StateMashine<State>> targets = new ArrayList<StateMashine<State>>();

        NodeList list = e.getElementsByTagName(ASSOCIATION);
        if (list == null) {
            return null;
        }

        for (int i = 0; i < list.getLength(); i++) {
            Element association = (Element) list.item(i);
            String ref = association.getAttribute(ATTR_TARGET);
            targets.add(stateMashines.get(ref));
        }
        return targets;
    }

    public Map<String, IControlledObject> readControlledObjects() throws AutomataFormatException {
        if (ctrlObjects != null) {
            return ctrlObjects;
        }
        ctrlObjects = new HashMap<String, IControlledObject>();
        try {
            NodeList list = document.getElementsByTagName(CTRL_OBJECT);
            if (list == null) {
                return null;
            }
            for (int i = 0; i < list.getLength(); i++) {
                NamedNodeMap attrs = list.item(i).getAttributes();
                String name = attrs.getNamedItem(ATTR_NAME).getNodeValue();
                String className = attrs.getNamedItem(ATTR_CLASS).getNodeValue();
                Class aClass = Class.forName(className);
                ctrlObjects.put(name, new ControlledObject(name, aClass));
            }
            return ctrlObjects;
        } catch (ClassNotFoundException e) {
            ctrlObjects = null;
            throw new AutomataFormatException("Conrolled object has unknown class", e);
        }
    }

    public Map<String, ? extends IStateMashine<? extends IState>> readStateMashines() throws AutomataFormatException {
        if (stateMashines == null) {
            ctrlObjects = readControlledObjects();
            eventProviders = readEventProviders();
            createStateMashines();

            NodeList list = document.getElementsByTagName(STATE_MASHINE);
            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                String name = parseStateMashineName((Element) list.item(i));
                StateMashine<State> m = stateMashines.get(name);

                parseCtrlObjects(m, e);

                Map<String, State> states = parseStates(m, e);
                m.addStates(states);
                addTransitions(m, e);
            }
        }
        return stateMashines;
    }

    public void createStateMashines() {
        if (stateMashines == null) {
            stateMashines = new HashMap<String, StateMashine<State>>();
            NodeList list = document.getElementsByTagName(STATE_MASHINE);
            for (int i = 0; i < list.getLength(); i++) {
                String name = parseStateMashineName((Element) list.item(i));
                stateMashines.put(name, new StateMashine<State>(name));
            }
        }
    }

    private String parseStateMashineName(Element e) {
        if (!StringUtils.equals(STATE_MASHINE, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't stateMashinet");
        }

        return e.getAttribute(ATTR_NAME);
    }

    private void parseCtrlObjects(StateMashine<? extends State> m, Element e) {
        if (!StringUtils.equals(STATE_MASHINE, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't stateMashine");
        }
        NodeList list = e.getElementsByTagName(ASSOCIATION);
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            String role = node.getAttribute(ATTR_SUPPLIER_ROLE);
            String target = node.getAttribute(ATTR_TARGET);

            IControlledObject ctrl = ctrlObjects.get(target);
            if (ctrl != null) {
                m.addControlledObject(role, ctrl);
            }
        }
    }

    private Map<String, State> parseStates(StateMashine<? extends State> m, Element e) throws AutomataFormatException {
        if (!StringUtils.equals(STATE_MASHINE, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't stateMashine");
        }
        Map<String, State> states = new HashMap<String, State>();

        NodeList list = e.getElementsByTagName(STATE);
        //First element is "Top" state, don't use it
        for (int i = 1; i < list.getLength(); i++) {
            State s = parseState(m, (Element) list.item(i));
            states.put(s.getName(), s);
        }
        return states;
    }

    private State parseState(StateMashine<? extends State> m, Element e) throws AutomataFormatException {
        if (!StringUtils.equals(STATE, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't state element");
        }
        String name = e.getAttribute(ATTR_NAME);
        String type = e.getAttribute(ATTR_TYPE);

        List<IAction> actions = new ArrayList<IAction>();
        NodeList list = e.getElementsByTagName(OUT_ACTION);
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            actions.add(parseAction(m, node));
        }
        State state = new State(name, StateType.getByName(type), actions);

        NodeList listSM = e.getElementsByTagName(STATE_MASHINE_REF);
        for (int i = 0; i < listSM.getLength(); i++) {
            Element node = (Element) listSM.item(i);
            String sm = node.getAttribute(ATTR_NAME);
            StateMashine<State> nested = stateMashines.get(sm);
            state.addNestedStateMashine(nested);
            nested.setParent(m, state);
        }

        return state;
    }

    /**
     * Parse transition in state <code>e</code> and add it to state mashine <code>m</code>.
     * I.e. add transition to source and target states
     * @param m state mashine
     * @param e element to be parsed
     * @throws AutomataFormatException
     */
    private void addTransitions(StateMashine<? extends State> m, Element e) throws AutomataFormatException {
        if (!StringUtils.equals(STATE_MASHINE, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't stateMashine");
        }

        NodeList list = e.getElementsByTagName(TRANSITION);
        for (int i = 0; i < list.getLength(); i++) {
            parseTransition(m, (Element) list.item(i));
        }
    }

    private void parseTransition(StateMashine<? extends State> m, Element e) throws AutomataFormatException {
        if (!StringUtils.equals(TRANSITION, e.getNodeName())) {
            throw new IllegalArgumentException("Element isn't transition");
        }

        String eventFullName = e.getAttribute(ATTR_EVENT);
        String condExpr = e.getAttribute(ATTR_COND);
        String source = e.getAttribute(ATTR_SOURCE);
        String target = e.getAttribute(ATTR_TARGET);

        State stateSource = m.getState(source);
        State stateTarget = m.getState(target);

        IEvent event = parseEvent(m, eventFullName);
        Transition t = new Transition(event, new Condition(condExpr), stateTarget);

        NodeList list = e.getElementsByTagName(OUT_ACTION);
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            t.addAction(parseAction(m, node));
        }
        stateSource.addOutcomingTransition(t);
    }

    /**
     * Extract controlled object and action from node attribute.
     * @param m state mashine
     * @param node node with action
     * @return action
     * @throws AutomataFormatException
     */
    private IAction parseAction(StateMashine<? extends State> m, Element node) throws AutomataFormatException {
        String actionFullName = node.getAttribute(ATTR_ACTION);
        if (!actionFullName.matches(StateMashine.METHOD_PATTERN)) {
            throw new AutomataFormatException("Wrong output action format: " + actionFullName);
        }
        int pointIndx = actionFullName.indexOf('.');
        String ctrlName = actionFullName.substring(0, pointIndx);
        String actionName = actionFullName.substring(pointIndx + 1);
        IControlledObject ctrlObj = m.getControlledObject(ctrlName);
        if (ctrlObj == null) {
            throw new AutomataFormatException("Unknown controlled object: "+ ctrlName);
        }
        IAction action = ctrlObj.getAction(actionName);
        if (action == null) {
            throw new AutomataFormatException("Unknown action name: "+ actionName);
        }
        return action;
    }

    /**
     * Extract event provider name and event name from eventAttr.
     * @param m state mashine
     * @param eventAttr event full qualifier
     * @return IEvent instance or null if event Attr is blank
     * @throws AutomataFormatException
     */
    private IEvent parseEvent(StateMashine<? extends State> m, String eventAttr) throws AutomataFormatException {
        String[] a = eventAttr.split("\\.");
        if (a.length > 2) {
            throw new AutomataFormatException("Wrong event format: " + eventAttr);
        }
        IEvent event;
        if (a.length == 2) {
            IEventProvider provider = eventProviders.get(a[0]);
            if (provider == null) {
                throw new AutomataFormatException("Unknown event provider: " + a[0]);
            }
            event = provider.getEvent(a[1]);
            if (event == null) {
                throw new AutomataFormatException("Unknown event name: " + a[1]);
            }
        } else if (StringUtils.isNotBlank(eventAttr)) {
            //try to find event in state mashine's event providers set
            event = findEventByName(m, eventAttr);
            if (event != null) {
                return event;
            }
        } else {
            return null;
        }
        
        throw new AutomataFormatException(String.format("Unknown event %s in state mashine %s", eventAttr, m.getName()));
    }

    private <M extends IStateMashine<? extends IState>> IEvent findEventByName(M m, String eventName) {
        for (IEventProvider p: m.getEventProviders()) {
            IEvent event = p.getEvent(eventName);
            if (event != null) {
                return event;
            }
        }
        return findEventByName(m.getParentStateMashine(), eventName);
    }
}
