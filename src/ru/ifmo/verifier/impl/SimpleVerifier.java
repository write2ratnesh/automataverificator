/**
 * SimpleVerifier.java, 06.04.2008
 */
package ru.ifmo.verifier.impl;

import ru.ifmo.verifier.IVerifier;
import ru.ifmo.verifier.IInterNode;
import ru.ifmo.verifier.ISharedData;
import ru.ifmo.verifier.concurrent.SharedData;
import ru.ifmo.verifier.automata.IntersectionAutomata;
import ru.ifmo.verifier.automata.IntersectionNode;
import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.translator.SimpleTranslator;
import ru.ifmo.ltl.buchi.translator.Ltl2baTranslator;
import ru.ifmo.ltl.grammar.LtlNode;
import ru.ifmo.ltl.grammar.LtlUtils;
import ru.ifmo.ltl.grammar.predicate.IPredicateFactory;
import ru.ifmo.ltl.LtlParseException;
import ru.ifmo.ltl.converter.ILtlParser;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class SimpleVerifier<S extends IState> implements IVerifier<S> {
    private S initState;
    private ILtlParser parser;
    private ITranslator translator;

    public SimpleVerifier(S initState) {
        this(initState, null);

    }

    public SimpleVerifier(S initState, ILtlParser parser) {
//        this(initState, parser, new SimpleTranslator());
        this(initState, parser, new Ltl2baTranslator());
    }

    public SimpleVerifier(S initState, ILtlParser parser, ITranslator translator) {
        if (initState == null) {
            throw new IllegalArgumentException("stateMashine can't be null");
        }
        if (translator == null) {
            throw new IllegalArgumentException("translator can't be null");
        }

        this.initState = initState;
        this.parser = parser;
        this.translator = translator;
    }

    public void setParser(ILtlParser parser) {
        this.parser = parser;
    }

    public List<IInterNode> verify(String ltlFormula, IPredicateFactory<S> predicates) throws LtlParseException {
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

    public List<IInterNode> verify(IBuchiAutomata buchi, IPredicateFactory<S> predicates) {
        IntersectionAutomata<S> automata = new IntersectionAutomata<S>(predicates, buchi);
        IntersectionNode initial = automata.getNode(initState, buchi.getStartNode(), 0);
        ISharedData sharedData = new SharedData(new HashSet<IntersectionNode>(), 0);
        Deque<? extends IInterNode> stack = new MainDfs(sharedData, 0).dfs(initial);

        List<IInterNode> res = new ArrayList<IInterNode>(stack.size());

        for (Iterator<? extends IInterNode> iter = stack.descendingIterator(); iter.hasNext();) {
            res.add(iter.next());
        }
        return res;
    }
}
