/*
 * Developed by eVelopers Corporation - 28.05.2008
 */
package ru.ifmo.util.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class DfsStackTreeNode<E> {

    private E item;
    private ConcurrentMap<E, DfsStackTreeNode<E>>
            children = new ConcurrentHashMap<E, DfsStackTreeNode<E>>();

    private DfsStackTreeNode<E> parent;

    public DfsStackTreeNode(E x) {
        this(x, null);
    }

    protected DfsStackTreeNode(E item, DfsStackTreeNode<E> parent) {
        this.item = item;
        this.parent = parent;
    }

    /**
     * Add children with item <code>item</code>
     * @param item item
     * @return children
     */
    public DfsStackTreeNode<E> addChildren(E item) {
        DfsStackTreeNode<E> child = new DfsStackTreeNode<E>(item, this);
        DfsStackTreeNode<E> res = children.putIfAbsent(item, new DfsStackTreeNode<E>(item, this));
        return (res == null) ? child : res;
    }

    public E getItem() {
        return item;
    }

    public Collection<DfsStackTreeNode<E>> getChildren() {
        return children.values();
    }

    public boolean hasChildren() {
        return children.isEmpty();
    }

    public boolean tryRemove() {
        if (parent != null) {
            if (children.isEmpty()) {
                return parent.children.remove(item) != null;
            }
        }
        return false;
    }
}
