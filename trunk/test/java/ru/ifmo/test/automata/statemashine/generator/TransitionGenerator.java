/**
 * TransitionGenerator.java, 11.06.2008
 */
package ru.ifmo.test.automata.statemashine.generator;

import ru.ifmo.test.automata.statemashine.impl.Transition;
import ru.ifmo.test.automata.statemashine.impl.StatemachineContext;
import ru.ifmo.test.automata.statemashine.impl.State;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public enum TransitionGenerator {

    NEW_TARGET(10) {
        public TransitionGenerator next() {
            return (rand.nextInt(MAX_P) < p) ? this : EXIST_TARGET;
        }

        public Transition getTransition(StatemachineContext context) {
            State target = context.generateState();
            return (target != null) ? generateTransition(context, target) : null;
        }
    },
    EXIST_TARGET(80) {
        public TransitionGenerator next() {
            return (rand.nextInt(MAX_P) < p) ? this : NEW_TARGET;
        }

        public Transition getTransition(StatemachineContext context) {
            List<State> states = context.getStates();
            State target = states.get(rand.nextInt(states.size()));
            return generateTransition(context, target);
        }
    };

    protected static final int MAX_P = 100;
    protected final int p;
    protected Random rand = new Random();

    private TransitionGenerator(int p) {
        this.p = p;
    }

    public abstract TransitionGenerator next();

    public abstract Transition getTransition(StatemachineContext context);

    protected Transition generateTransition(StatemachineContext context, State target) {
        //last event isn't used
        String e = context.getEvents().get(rand.nextInt(context.getEvents().size() - 1));
        int n = (int) (Math.abs(rand.nextGaussian()) + 2);
        List<String> z = new ArrayList<String>(n);
        for (int i = 0; i < n; i++) {
            //last action isn't used
            z.add(context.getActions().get(rand.nextInt(context.getActions().size() - 1)));
        }
        return new Transition(e, z, target);
    }
}
