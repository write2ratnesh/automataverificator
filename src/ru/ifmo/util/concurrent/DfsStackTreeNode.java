/*
 * Developed by eVelopers Corporation - 28.05.2008
 */
package ru.ifmo.util.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Collection;

public class DfsStackTreeNode<E> {

    private int threadNumber;
    private E item;
    private ConcurrentMap<E, DfsStackTreeNode<E>> children;

    private DfsStackTreeNode<E> parent;

    public final AtomicBoolean visited = new AtomicBoolean(false);

    public DfsStackTreeNode(E x, int threadNumber) {
        this(x, null, threadNumber);
    }

    protected DfsStackTreeNode(E item, DfsStackTreeNode<E> parent, int threadNumber) {
        this.item = item;
        this.parent = parent;
        this.threadNumber = threadNumber;
        children = new ConcurrentHashMap<E, DfsStackTreeNode<E>>((threadNumber * 4) / 3, 0.75f, threadNumber);
    }

    /**
     * Add children with item <code>item</code>
     * @param item item
     * @return children
     */
    public DfsStackTreeNode<E> addChild(E item) {
        DfsStackTreeNode<E> child = new DfsStackTreeNode<E>(item, this, threadNumber);
        DfsStackTreeNode<E> res = children.putIfAbsent(item, child);
        return (res == null) ? child : res;
    }

    public E getItem() {
        return item;
    }

    public DfsStackTreeNode<E> getParent() {
        return parent;
    }

    public Collection<DfsStackTreeNode<E>> getChildren() {
        return children.values();
    }

    public boolean hasChildren() {
        return children.isEmpty();
    }

    /**
     * Try to remove node. If children have been visited, than remove node
     * @return true, if node was successfuly removed
     */
    public boolean tryRemove() {
        if (parent != null && children.isEmpty()) {
            return parent.children.remove(item) != null;
        }
        return false;
    }
}
