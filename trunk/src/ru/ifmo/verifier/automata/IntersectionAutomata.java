/**
 * IntersectionAutomata.java, 12.04.2008
 */
package ru.ifmo.verifier.automata;

import ru.ifmo.automata.statemashine.IState;
import ru.ifmo.ltl.buchi.IBuchiNode;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.grammar.predicate.IPredicateUtils;

import java.util.*;

/**
 * TODO: add comment
 *
 * @author: Kirill Egorov
 */
public class IntersectionAutomata<S extends IState> implements IIntersectionAutomata<S> {

    private IPredicateUtils<S> predicates;
    private IBuchiAutomata buchiAutomata;

    private Set<IntersectionNode<S>> nodes = new HashSet<IntersectionNode<S>>();
    private Map<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>> nodeMap
            = new HashMap<S, Map<IBuchiNode, Map<Integer, IntersectionNode<S>>>>();

    public IntersectionAutomata(IPredicateUtils<S> predicates, IBuchiAutomata buchi) {
        if (buchi == null || predicates == null) {
            throw new IllegalArgumentException();
        }
        this.predicates = predicates;
        this.buchiAutomata = buchi;
    }

    public IBuchiAutomata getBuchiAutomata() {
        return buchiAutomata;
    }

    public IntersectionNode<S> getNode(S state, IBuchiNode node, int acceptSet) {
        Map<IBuchiNode, Map<Integer, IntersectionNode<S>>> buchiMap = nodeMap.get(state);
        if (buchiMap == null) {
            buchiMap = new HashMap<IBuchiNode, Map<Integer, IntersectionNode<S>>>();
            nodeMap.put(state, buchiMap);
        }

        Map<Integer, IntersectionNode<S>> acceptMap = buchiMap.get(node);
        if (acceptMap == null) {
            acceptMap = new HashMap<Integer, IntersectionNode<S>>();
            buchiMap.put(node, acceptMap);
        }

        IntersectionNode<S> res = acceptMap.get(acceptSet);
        if (res == null) {
            res = new IntersectionNode<S>(this, state, node, acceptSet);
            acceptMap.put(acceptSet, res);
            nodes.add(res);
        }
        return res;
    }

    public Set<IntersectionNode<S>> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public IPredicateUtils<S> getPredicates() {
        return predicates;
    }
}
