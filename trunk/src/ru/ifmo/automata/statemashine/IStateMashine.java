/**
 * IStateMashine.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * State mashine
 *
 * @author: Kirill Egorov
 */
public interface IStateMashine<S extends IState> {

    String getName();

    IStateMashine<? extends S> getParentStateMashine();

    S getParentState();

    boolean isNested();

    S getInitialState();

    S getState(String stateName);

    Collection<S> getStates();

    Set<IEventProvider> getEventProviders();

    IControlledObject getControlledObject(String association);

    Collection<IControlledObject> getControlledObjects();

    /**
     * Get sources for condition evaluation.
     * @return
     */
    Map<String, Map<String, ?>> getSources();
}
