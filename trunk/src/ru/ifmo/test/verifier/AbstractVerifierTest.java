/**
 * AbstractVerifierTest.java, 26.04.2008
 */
package ru.ifmo.test.verifier;

import junit.framework.TestCase;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.automata.IIntersectionTransition;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.io.StateMachineReader;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMachine;

import java.util.List;
import java.io.IOException;

/**
 * Abstract verifier test
 *
 * @author Kirill Egorov
 */
public abstract class AbstractVerifierTest<S extends IState> extends TestCase {
    protected final String xmlFileName;
    protected final String stateMachineName;

    protected IVerifier<S> verifier;
    protected IPredicateFactory<S> predicates;

    protected AbstractVerifierTest(String xmlFileName, String stateMachineName) {
        super();
        this.xmlFileName = xmlFileName;
        this.stateMachineName = stateMachineName;
    }

    protected void setUp() throws IOException, AutomataFormatException {
        predicates = createPredicateUtils();
        IAutomataContext context = new AutomataContext(new StateMachineReader(xmlFileName));
        ILtlParser parser = new LtlParser(context, predicates);
        verifier = createVerifier(context.getStateMachine(stateMachineName), parser);
    }

    protected abstract IVerifier<S> createVerifier(IStateMachine<? extends IState> stateMachine, ILtlParser parser);

    protected abstract IPredicateFactory<S> createPredicateUtils();

    protected void printStack(List<IIntersectionTransition> stack) {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty");
        } else {
            System.out.println("DFS 1 stack:");
        }
        final int MAX_LEN = 80;
        int len = 0;
        for (IIntersectionTransition trans: stack) {
            String tmp = trans.getTarget().toString();
            len += tmp.length();
            if (len > MAX_LEN) {
                len = tmp.length();
                System.out.println();
            }
            System.out.print("-->" + tmp);
        }
        System.out.println();
    }
}
