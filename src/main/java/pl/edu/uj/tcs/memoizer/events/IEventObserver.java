package pl.edu.uj.tcs.memoizer.events;

/**
 * 
 * @author ziemin
 * 
 * Observer class for events of specified type. 
 *
 * @param <T> class implementing IEvent
 */
public interface IEventObserver <T extends IEvent> {

	/**
	 * Notifies observer about new event occurrence
	 * @param event
	 */
	public void notify(T event);
}
