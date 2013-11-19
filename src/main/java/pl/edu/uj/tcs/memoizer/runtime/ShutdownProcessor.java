package pl.edu.uj.tcs.memoizer.runtime;

import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.communication.IMemeProvider;

/**
 * Class responsible for finishing and closing all operations
 * when application is being closed
 * @author ziemin
 *
 */
public class ShutdownProcessor implements IEventObserver<ShutdownEvent> {
	
	private IEventService eventService;
	private IMemeProvider memeProvider;
	
	public ShutdownProcessor(IEventService eventService, IMemeProvider memeProvider) {
		this.eventService = eventService;
		this.memeProvider = memeProvider;
	}

	@Override
	public void notify(ShutdownEvent event) {

		memeProvider.stop();
		eventService.stop();
	}

}
