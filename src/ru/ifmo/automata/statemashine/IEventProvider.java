/**
 * IEventProvider.java, 01.03.2008
 */
package ru.ifmo.automata.statemashine;

import ru.ifmo.automata.statemashine.IEvent;

import java.util.Collection;

/**
 * TODO: add comment
 *
 * @author Kirill Egorov
 */
public interface IEventProvider {

    public String getName();

    public IEvent getEvent(String eventName);

    public Collection<IEvent> getEvents();

//    public Collection<IStateMachine> getTargets();

    public Class getImplClass();
}
