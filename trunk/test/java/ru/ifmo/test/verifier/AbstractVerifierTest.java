/**
 * AbstractVerifierTest.java, 26.04.2008
 */
package ru.ifmo.test.verifier;

import junit.framework.TestCase;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.IVerifier;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.converter.ILtlParser;
import ru.ifmo.ltl.converter.LtlParser;
import ru.ifmo.automata.statemashine.impl.AutomataFormatException;
import ru.ifmo.automata.statemashine.impl.AutomataContext;
import ru.ifmo.automata.statemashine.impl.UnimodXmlReader;
import ru.ifmo.automata.statemashine.IAutomataContext;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.automata.statemashine.IStateMashine;

import java.util.List;
import java.io.IOException;

/**
 * Abstract verifier test
 *
 * @author: Kirill Egorov
 */
public abstract class AbstractVerifierTest<S extends IState> extends TestCase {
    protected String xmlFileName;
    protected String stateMashineName;

    protected IVerifier<S> verifier;
    protected IPredicateFactory<S> predicates;

    protected AbstractVerifierTest(String xmlFileName, String stateMashineName) {
        super();
        this.xmlFileName = xmlFileName;
        this.stateMashineName = stateMashineName;
    }

    protected void setUp() throws IOException, AutomataFormatException {
        predicates = createPredicateUtils();
        IAutomataContext context = new AutomataContext(new UnimodXmlReader(xmlFileName));
        ILtlParser parser = new LtlParser(context, predicates);
        verifier = createVerifier(context.getStateMashine(stateMashineName), parser);
    }

    protected abstract IVerifier<S> createVerifier(IStateMashine<? extends IState> stateMashine, ILtlParser parser);

    protected abstract IPredicateFactory<S> createPredicateUtils();

    protected void printStack(List<IInterNode> stack) {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty");
        } else {
            System.out.println("DFS 1 stack:");
        }
        final int MAX_LEN = 80;
        int len = 0;
        for (IInterNode node: stack) {
            String tmp = node.toString();
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
