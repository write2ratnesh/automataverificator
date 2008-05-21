package ru.ifmo.verifier;

import ru.ifmo.verifier.automata.IntersectionNode;

public interface IDfs<R> {

    public R dfs(IntersectionNode node);
}
