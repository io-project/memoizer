package pl.edu.uj.tcs.memoizer.events;

import java.util.List;

import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

public interface IEventService {

    /**
     * Registers observer for event of type T
     * 
     * @returns true if attaching operation was successful
     */
    <T extends IEvent> boolean attach(IEventObserver<T> observer);

    /**
     * @returns true if detaching operation was successful
     */
    <T extends IEvent> boolean detach(IEventObserver<T> observer);

    /**
     * @returns returns a list of all provided observers
     */
    List<IEventObserver<? extends IEvent>> listObservers();

    /**
     * Dispatches an event through the service
     * 
     * @throws EventException
     *             when no observer for given event is registered
     */
    void call(IEvent event) throws EventException;
    
    /**
     * Stops entire event service. It is not usable any longer.
     */
    void stop();

}
