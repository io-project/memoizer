package pl.edu.uj.tcs.memoizer.events;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 
 * @author ziemin
 * 
 * Interface enabling event to act as synchronous method call
 * thanks to usage of a future. Since single future is returned
 * by this event, only one observer registered at EventService for this particular event 
 * will be able to provide result of computation
 *
 * @param <T> is a type of result of an action triggered by this event
 */
public abstract class AbstractFutureEvent <T> implements IEvent {
	
	private CallableEventSetter setter = new CallableEventSetter();
	public FutureTask<T> task = new FutureTask<T>(setter);


	/**
	 * @return future being a desired result of computation
	 */
	public Future<T> getFutureResult() {
		return task;
	}
	
	/**
	 * Method meant to be called only by observer when its done
	 * to set the result for an event creator
	 * @param result of computation
	 */
	public void setResult(T result) {

		setter.result = result;
		task.run();
	}
	
	private class CallableEventSetter implements Callable<T> {

		private T result;

		@Override
		public T call() throws Exception {
			return result;
		}
		
	}
}
