package pl.edu.uj.tcs.memoizer.events;

import java.util.List;

import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

public interface IEventService {

	/**
	 * Registers observer for event of type T
	 * @returns true if attaching operation was successful
	 */
	public <T extends IEvent> boolean attach(IEventObserver<T> observer);

	/**
	 * @returns true if detaching operation was successful
	 */
	public <T extends IEvent> boolean detach(IEventObserver<T> observer);

	/**
	 * @returns returns a list of all provided observers
	 */
	public List<IEventObserver<? extends IEvent>> listObservers();

	/**
	 * Dispatches an event through the service
	 * @throws EventException when no observer for given event is registered
	 */
	public void call(IEvent event) throws EventException;
	
}
