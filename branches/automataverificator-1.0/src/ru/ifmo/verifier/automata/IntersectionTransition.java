/**
 * DfsTransition.java, 12.04.2008
 */
package ru.ifmo.verifier.automata;

import ru.ifmo.automata.ITransition;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public class IntersectionTransition implements ITransition<IntersectionNode> {

    private IntersectionNode target;

    public IntersectionTransition(IntersectionNode target) {
        if (target == null) {
            throw new IllegalArgumentException("Target can't be null");
        }
        this.target = target;
    }

    public IntersectionNode getTarget() {
        return target;
    }
}
