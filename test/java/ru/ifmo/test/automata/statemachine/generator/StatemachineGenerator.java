/**
 * StatemachineGenerator.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemachine.generator;

import ru.ifmo.test.automata.statemachine.impl.*;

import java.io.IOException;
import java.io.File;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class StatemachineGenerator {

    public static void main(String[] args) throws IOException {
        generateStateMashine(1000);
        generateStateMashine(5000);
        generateStateMashine(10000);
        generateStateMashine(15000);
        generateStateMashine(20000);
        generateStateMashine(25000);
    }

    public static void generateStateMashine(int transitionCount) throws IOException {
        StatemachineContext context = new StatemachineContext(TestEventProvider.class, TestControlledObject.class);

        State curState = context.getInitState();
        long tCount = 0;
        for (TransitionGenerator g = TransitionGenerator.NEW_TARGET; tCount < transitionCount; g = g.next()) {
            Transition t = g.getTransition(context);
            if (t == null) {
                break;
            }
            curState.addTransition(t);
            curState = t.getTarget();
            tCount++;
        }
        StatemachineWriter.getInstance().write(
                String.format("test//resources//A%d.xml", transitionCount),
                context);
        System.out.println("State: " + context.getStates().size());
        System.out.println("Transitions: " + tCount);
    }
}
