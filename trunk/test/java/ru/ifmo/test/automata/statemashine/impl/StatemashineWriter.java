/**
 * StatemashineWriter.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.impl;

import java.io.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class StatemashineWriter {
    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE model PUBLIC \"-//eVelopers Corp.//DTD State machine model V1.0//EN\" \"http://www.evelopers.com/dtd/unimod/statemachine.dtd\">\n" +
            "<model name=\"Model1\">\n" +
            "  <controlledObject class=\"%s\" name=\"o1\"/>\n" +
            "  <eventProvider class=\"%s\" name=\"p1\">\n" +
            "    <association clientRole=\"p1\" targetRef=\"A1\"/>\n" +
            "  </eventProvider>\n" +
            "  <rootStateMachine>\n" +
            "    <stateMachineRef name=\"A1\"/>\n" +
            "  </rootStateMachine>\n" +
            "  <stateMachine name=\"A1\">\n" +
            "    <configStore class=\"com.evelopers.unimod.runtime.config.DistinguishConfigManager\"/>\n" +
            "    <association clientRole=\"A1\" supplierRole=\"o1\" targetRef=\"o1\"/>\n" +
            "    <state name=\"Top\" type=\"NORMAL\">\n";
    private static final String END = "  </stateMachine>\n" +
            "</model>";

    private static final String STATE_TOP_END = "    </state>\n";
    private static final String INIT_STATE = "      <state name=\"%s\" type=\"INITIAL\"/>\n";
    private static final String STATE = "      <state name=\"%s\" type=\"NORMAL\"/>\n";
    private static final String TRANSITION = "    <transition event=\"%s\" sourceRef=\"%s\" targetRef=\"%s\">\n";
    private static final String TRANSITION_END = "    </transition>\n";
    private static final String ACTION = "      <outputAction ident=\"o1.%s\"/>\n";

    private static final StatemashineWriter instance = new StatemashineWriter();

    public static StatemashineWriter getInstance() {
        return instance;
    }

    private StatemashineWriter() {}

    public void write(String fileName, StatemashineContext context) throws IOException {
        Writer out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fileName)), "utf-8");
        out.write(String.format(START, context.getControlledObjectClass().getName(), context.getEventProviderClass().getName()));

        out.write(String.format(INIT_STATE, context.getInitState().getName()));
        for (State s: context.getStates()) {
            out.write(String.format(STATE, s.getName()));
        }
        out.write(STATE_TOP_END);

        writeTransitions(out, context.getInitState());
        for (State s: context.getStates()) {
            writeTransitions(out, s);
        }

        out.write(END);
        out.flush();
        out.close();
    }

    protected void writeTransitions(Writer out, State s) throws IOException {
        for (Transition t: s.getTransitions()) {
            out.write(String.format(TRANSITION, t.getEvent(), s.getName(), t.getTarget().getName()));
            for (String z: t.getActions()) {
                out.write(String.format(ACTION, z));
            }
            out.write(String.format(TRANSITION_END));
        }
    }
}
