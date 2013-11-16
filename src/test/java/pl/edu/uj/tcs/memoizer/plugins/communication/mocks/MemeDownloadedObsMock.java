package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.plugins.communication.MemeDownloadedEvent;

public class MemeDownloadedObsMock implements IEventObserver<MemeDownloadedEvent> {
	
	private int counter;
	
	public MemeDownloadedObsMock() {
		counter = 0;
	}
	
	public int getCounter() {
		return counter;
	}

	@Override
	public void notify(MemeDownloadedEvent event) {
		counter++;
	}
}
