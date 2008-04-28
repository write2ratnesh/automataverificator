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
public class IntersectionAutomata {

    private IPredicateUtils predicates;
    private IBuchiAutomata buchiAutomata;

    private Set<IntersectionNode> nodes = new HashSet<IntersectionNode>();
    private Map<IState, Map<IBuchiNode, Map<Integer, IntersectionNode>>> nodeMap
            = new HashMap<IState, Map<IBuchiNode, Map<Integer, IntersectionNode>>>();

    public IntersectionAutomata(IPredicateUtils predicates, IBuchiAutomata buchi) {
        if (buchi == null) {
            throw new IllegalArgumentException("Buchi automata can't be null");
        }
        if (predicates == null) {
            throw new IllegalArgumentException("predicates can't be null");
        }
        this.predicates = predicates;
        this.buchiAutomata = buchi;
    }

    public IBuchiAutomata getBuchiAutomata() {
        return buchiAutomata;
    }

    public IntersectionNode getNode(IState state, IBuchiNode node, int acceptSet) {
        Map<IBuchiNode, Map<Integer, IntersectionNode>> buchiMap = nodeMap.get(state);
        if (buchiMap == null) {
            buchiMap = new HashMap<IBuchiNode, Map<Integer, IntersectionNode>>();
            nodeMap.put(state, buchiMap);
        }

        Map<Integer, IntersectionNode> acceptMap = buchiMap.get(node);
        if (acceptMap == null) {
            acceptMap = new HashMap<Integer, IntersectionNode>();
            buchiMap.put(node, acceptMap);
        }

        IntersectionNode res = acceptMap.get(acceptSet);
        if (res == null) {
            res = new IntersectionNode(this, state, node, acceptSet);
            acceptMap.put(acceptSet, res);
            nodes.add(res);
        }
        return res;
    }

    public Set<IntersectionNode> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public IPredicateUtils getPredicates() {
        return predicates;
    }
}
