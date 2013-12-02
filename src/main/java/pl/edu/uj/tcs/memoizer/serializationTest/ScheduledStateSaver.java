package pl.edu.uj.tcs.memoizer.serializationTest;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.runtime.ShutdownEvent;

/**
 * Class destined to save state of different objects periodically 
 * by sending events
 * @author ziemin
 *
 */
public class ScheduledStateSaver implements IEventObserver<ShutdownEvent> {
	
	private Logger LOG = Logger.getLogger(ScheduledStateSaver.class);
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	private IEventService es;
	
	/**
	 * @param es
	 * @param saveRate of event passing in milliseconds
	 */
	public ScheduledStateSaver(IEventService es, long saveRate) {
		this.es = es;
		
		runTask(saveRate);
	}

	public void changeSaveRate(long millis) {

		executor.shutdown();
		runTask(millis);
	}
	
	private void runTask(long rate) {

		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new Saver(), 0, rate, TimeUnit.MILLISECONDS);
	}
	
	private class Saver implements Runnable {
		@Override
		public void run() {
			try {
				ScheduledStateSaver.this.es.call(new SaveStateEvent());
			} catch (EventException e) {
				LOG.error("Cannot pass event: " + ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@Override
	public void notify(ShutdownEvent event) {
		executor.shutdownNow();
	}
}
