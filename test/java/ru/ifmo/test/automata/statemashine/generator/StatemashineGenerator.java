/**
 * StatemashineGenerator.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.generator;

import ru.ifmo.test.automata.statemashine.impl.*;

import java.io.IOException;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class StatemashineGenerator {

    public static void main(String[] args) throws IOException {
        final int transitionCount = 30000;
        StatemashineContext context = new StatemashineContext(TestEventProvider.class, TestControlledObject.class);

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
        StatemashineWriter.getInstance().write("test\\resources\\A" + transitionCount + ".xml", context);
        System.out.println("State: " + context.getStates().size());
        System.out.println("Transitions: " + tCount);
    }
}
