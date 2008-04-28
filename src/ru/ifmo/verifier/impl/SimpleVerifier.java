/**
 * SimpleVerifier.java, 06.04.2008
 */
package ru.ifmo.verifier.impl;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.automata.IntersectionAutomata;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.automata.statemashine.IStateMashine;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.converter.ILtlParser;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class SimpleVerifier implements IVerifier {
    private IStateMashine<? extends IState> stateMashine;
    private ILtlParser parser;
    private ITranslator translator = new SimpleTranslator();

    public SimpleVerifier(IStateMashine<? extends IState> stateMashine) {
        this(stateMashine, null);

    }

    public SimpleVerifier(IStateMashine<? extends IState> stateMashine, ILtlParser parser) {
        this(stateMashine, parser, new SimpleTranslator());
    }

    public SimpleVerifier(IStateMashine<? extends IState> stateMashine, ILtlParser parser, ITranslator translator) {
        if (stateMashine == null) {
            throw new IllegalArgumentException("stateMashine can't be null");
        }
        if (translator == null) {
            throw new IllegalArgumentException("translator can't be null");
        }

        this.stateMashine = stateMashine;
        this.parser = parser;
        this.translator = translator;
    }

    public void setParser(ILtlParser parser) {
        this.parser = parser;
    }

    public List<IInterNode> verify(String ltlFormula, IPredicateUtils predicates) throws LtlParseException {
        if (parser == null) {
            throw new UnsupportedOperationException("Can't verify LTL formula without LTL parser."
                    + "Define it first or use List<IStateTransition> verify(IBuchiAutomata buchi) method instead");
        }
        LtlNode ltl = parser.parse(ltlFormula);
        ltl = LtlUtils.getInstance().neg(ltl);
        IBuchiAutomata buchi = translator.translate(ltl);

        //TODO: -----------------------
        System.out.println("LTL: " + ltlFormula);
        System.out.println(buchi);
        //-----------------------------
        
        return verify(buchi, predicates);
    }

    public List<IInterNode> verify(IBuchiAutomata buchi, IPredicateUtils predicates) {
        IntersectionAutomata automata = new IntersectionAutomata(predicates, buchi);
        IntersectionNode initial = automata.getNode(stateMashine.getInitialState(), buchi.getStartNode(), 0);

        Deque<? extends IInterNode> stack = new Dfs1().dfs(initial);

        List<IInterNode> res = new ArrayList<IInterNode>(stack.size());
        for (Iterator<? extends IInterNode> iter = stack.descendingIterator(); iter.hasNext();) {
            res.add(iter.next());
        }
        return res;
    }

    private class Dfs1 extends AbstractDfs<Deque<IntersectionNode>> {
        private Dfs1() {
            setResult(getStack());
        }

        protected boolean leaveNode(IntersectionNode node) {
            if (node.isTerminal()) {
                AbstractDfs<Boolean> dfs2 = new Dfs2(getStack());
                if (dfs2.dfs(node)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class Dfs2 extends AbstractDfs<Boolean> {
        private Deque<IntersectionNode> dfsStack;

        private Dfs2(Deque<IntersectionNode> dfsStack) {
            this.dfsStack = dfsStack;
            setResult(false);
        }

        protected void enterNode(IntersectionNode node) {
            node.resetIterator();
        }

        protected boolean visitNode(IntersectionNode node) {
            if (dfsStack.contains(node)) {
                setResult(true);

                //TODO: del stack print  ------------------------

                if (getStack().isEmpty()) {
                    System.out.println("Stack is empty");
                }
                final int MAX_LEN = 80;
                int len = 0;
                for (IInterNode n: getStack()) {
                    String tmp = n.toString();
                    len += tmp.length();
                    if (len > MAX_LEN) {
                        len = tmp.length();
                        System.out.println();
                    }
                    System.out.print("-->" + tmp);
                }
                System.out.println("-->" + node);
                //-----------------------------------------------
                return true;
            }
            return false;
        }
    }
}
